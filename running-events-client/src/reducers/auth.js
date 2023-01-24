import {
    LOGIN_SUCCESS,
    LOGIN_FAIL,
    LOGOUT,
} from "../actions/types";
import { getUserFromCookie } from "../helpers/cookieHelper";

const user = getUserFromCookie();

const initialState = user
    ? { isLoggedIn: true, user }
    : { isLoggedIn: false, user: null };

function authReducer(state = initialState, action) {
    const { type, payload } = action;

    switch (type) {
        case LOGIN_SUCCESS:
            return {
                ...state,
                isLoggedIn: true,
                user: payload
            };
        case LOGIN_FAIL:
            return {
                ...state,
                isLoggedIn: false,
                token: null,
            };
        case LOGOUT:
            return {
                ...state,
                isLoggedIn: false,
                token: null,
            };
        default:
            return state;
    }
}

export default authReducer;