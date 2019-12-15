package com.gablum.usermanagement.user.security;

import com.gablum.usermanagement.user.model.Role;
import com.gablum.usermanagement.user.model.User;
import com.gablum.usermanagement.user.model.othermodels.Proposal;
import com.gablum.usermanagement.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class UserListService {

    @Autowired
    public SimpMessageSendingOperations sendingOperations;

    @Autowired
    public UserRepository userRepository;

    public void postMessageToUserListChannel(Proposal proposal){
        List<User> listOfUsers = userRepository.findAllByBusinessSubDomain(proposal.getBusinessSubDomain());
        log.warn(
                "\n\n\n\n\n\n" + "recd props" + "\n\n\n\n\n"
        );

        for(int i = 0; i < listOfUsers.size(); i++){
            boolean isSeller = false;
            for(Role role : listOfUsers.get(i).getRole()) {
                if(role.getRole().contains("seller")){
                    isSeller = true;
                }
                if(isSeller){
                sendingOperations.convertAndSend(
                    "/topic/proposalAlert/" + listOfUsers.get(0).getEmail(),
                     "hello"
                    );
                }
            }
        }
    }

}
