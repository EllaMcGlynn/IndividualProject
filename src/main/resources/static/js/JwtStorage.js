const JwtStorage = {
	JWT_KEY: "jwtToken",
	
	saveJwt: function (jwt) {
		localStorage.setItem(this.JWT_KEY, jwt);
	},
	
	getJwt: function () {
		return localStorage.getItem(this.JWT_KEY);
	},
	
	getUserRole: function () {
	    const jwt = this.getJwt();
	    if (!jwt) return null;

	    const payload = jwt.split('.')[1];
	    const decodedPayload = JSON.parse(atob(payload.replace(/-/g, '+').replace(/_/g, '/')));
	    
	    return decodedPayload.role || null;
	},

	getUserId: function () {
	    const jwt = this.getJwt();
	    if (!jwt) return null;

	    const payload = jwt.split('.')[1];
	    const decodedPayload = JSON.parse(atob(payload.replace(/-/g, '+').replace(/_/g, '/')));

	    return decodedPayload.userId || null;  // Ensure this matches the key in your JWT payload
	}
};
