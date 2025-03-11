const JwtStorage = {
	JWT_KEY: "jwtToken",
	
	saveJwt: function (jwt) {
		localStorage.setItem(this.JWT_KEY, jwt);
	},
	
	getJwt: function () {
		return localStorage.getItem(this.JWT_KEY);
	},
	
	// Method to extract user role from the JWT
	getUserRole: function () {
	    const jwt = this.getJwt();
	    if (!jwt) {
	        return null;
	    }
	    // Split the JWT into three parts (header, payload, signature)
	    const payload = jwt.split('.')[1];

	    // Decode the payload from Base64 URL encoding
	    const decodedPayload = JSON.parse(atob(payload.replace(/-/g, '+').replace(/_/g, '/')));

	    // Return the role from the decoded payload (you can change 'role' to whatever the actual key is in your payload)
	    return decodedPayload.role || null;
	}

}