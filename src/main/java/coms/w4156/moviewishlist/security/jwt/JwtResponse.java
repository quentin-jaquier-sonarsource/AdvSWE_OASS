package coms.w4156.moviewishlist.security.jwt;

import java.io.Serializable;

public class JwtResponse implements Serializable {
    private static final long serialVersionUID = 40572309523509237L;
    private final String jwtToken;

    public JwtResponse(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getToken() {
		return this.jwtToken;
	}
}
