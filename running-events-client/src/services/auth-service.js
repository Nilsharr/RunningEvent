import serverInstance from "../server/server";
import { getUserFromCookie } from "../helpers/cookieHelper";

const signup = (email, password, confirmPassword) => {
    return serverInstance.post("/users/signup", { email, password, confirmPassword });
};

const login = (email, password) => {
    return serverInstance.post("/users/login", { email, password });
};

const changePassword = (currentPassword, newPassword, confirmNewPassword) => {
    const user = getUserFromCookie();
    const authorizationToken = user ? { Authorization: `Bearer ${user.token}` } : {};
    return serverInstance.patch("/users/change-password", { currentPassword, newPassword, confirmNewPassword }, { headers: authorizationToken });
}

const verifyToken = token => {
    return serverInstance.post("/users/verify-token", { token })
}

const authService = {
    signup,
    login,
    changePassword,
    verifyToken,
};

export default authService;