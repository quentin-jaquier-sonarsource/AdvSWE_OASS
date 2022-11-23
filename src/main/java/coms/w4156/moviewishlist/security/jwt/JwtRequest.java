package coms.w4156.moviewishlist.security.jwt;

import java.io.Serializable;

public class JwtRequest implements Serializable {
    public static final long serialVersionUID = 249873503498257L;

    private String email;

    public JwtRequest() {}

    public JwtRequest(String email, String password) {
        this.setEmail(email);
    }

    public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
