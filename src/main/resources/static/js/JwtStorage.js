const JwtStorage = {
	JWT_KEY: "jwt_for_individual",
	
	saveJwt: function (jwt) {
		localStorage.setItem(this.JWT_KEY, jwt);
	}
}