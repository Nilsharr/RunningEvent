import Cookies from 'js-cookie';

export const getUserFromCookie = () => {
    const userCookie = Cookies.get("user");
    if (userCookie !== undefined) {
        return JSON.parse(userCookie);
    }
    return null;
}