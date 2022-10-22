package com.flab.livecommerce.domain.user;

import com.flab.livecommerce.common.AuthenticatedUser;

public interface TokenRepository {

    void save(String token, User user);

    AuthenticatedUser findByToken(String token);

    void remove(String token);
}
