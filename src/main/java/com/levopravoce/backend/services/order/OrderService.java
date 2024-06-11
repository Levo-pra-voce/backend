package com.levopravoce.backend.services.order;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.Order;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.repository.OrderRepository;
import com.levopravoce.backend.services.map.GoogleMapsService;
import com.levopravoce.backend.services.map.dto.LatLngDTO;
import com.levopravoce.backend.services.order.mapper.OrderMapper;
import com.levopravoce.backend.services.order.utils.OrderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.levopravoce.backend.services.order.dto.OrderDTO;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderUtils orderUtils;
    private final GoogleMapsService googleMapsService;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        orderUtils.validateNewOrder(orderDTO);

        var result = googleMapsService.getDistance(LatLngDTO.builder()
                        .lat(orderDTO.getOriginLatitude())
                        .lng(orderDTO.getOriginLongitude())
                        .build(),
                LatLngDTO.builder()
                        .lat(orderDTO.getDestinationLatitude())
                        .lng(orderDTO.getDestinationLongitude())
                        .build());
        if(result == null){
            throw new IllegalArgumentException("Erro ao buscar distância da API do Google Maps.");
        }

        orderDTO.setDistance(result.getDistanceValueMeters());
        orderDTO.setDuration(result.getDurationValueSeconds());

        User currentUser = SecurityUtils.getCurrentUser().orElseThrow();
        if (!Objects.equals(currentUser.getUserType(), UserType.CLIENTE)) {
            throw new RuntimeException("Apenas clientes podem criar pedidos.");
        }

        boolean existsByStatusInProgressOrPending = orderRepository.existsByStatusInProgressOrPending(currentUser.getId());
        if (existsByStatusInProgressOrPending) {
            throw new RuntimeException("Você já possui um pedido em andamento ou pendente.");
        }

        Order order = orderMapper.toEntity(orderDTO);
        order.setClient(currentUser);
        return orderMapper.toDTO(orderRepository.save(order));
    }



}
