package com.levopravoce.backend.services.request;

import com.levopravoce.backend.common.SecurityUtils;
import com.levopravoce.backend.entities.Request;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.entities.UserType;
import com.levopravoce.backend.repository.RequestRepository;
import com.levopravoce.backend.repository.UserRepository;
import com.levopravoce.backend.services.request.dto.RequestDTO;
import com.levopravoce.backend.services.request.mapper.RequestMapper;
import com.levopravoce.backend.services.request.utils.RequestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final RequestUtils requestUtils;
    private final UserRepository userRepository;

    public RequestDTO createRequest(RequestDTO requestDTO) {
        requestUtils.validateNewRequest(requestDTO);

        User currentUser = SecurityUtils.getCurrentUser().orElseThrow();
        if (!Objects.equals(currentUser.getUserType(), UserType.ENTREGADOR)) {
            throw new RuntimeException("Apenas entregadores podem aceitar pedidos.");
        }

        boolean existsByStatusInProgressOrPending = requestRepository.existsByStatusInProgressOrPending(
                requestDTO.getOrderId());
        if (existsByStatusInProgressOrPending) {
            throw new RuntimeException("Este pedido já foi aceito ou está em andamento.");
        }

        Request request = requestMapper.toEntity(requestDTO);
        request.setDeliveryman(currentUser);
        return requestMapper.toDTO(requestRepository.save(request));
    }
}
