const JwtStorage = {
	JWT_KEY: "jwtToken",
	
	saveJwt: function (jwt) {
		localStorage.setItem(this.JWT_KEY, jwt);
	},
	
	getJwt: function () {
		return localStorage.getItem(this.JWT_KEY);
	}
}