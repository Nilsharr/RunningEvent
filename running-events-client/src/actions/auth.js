import {
    LOGIN_SUCCESS,
    LOGIN_FAIL,
    LOGOUT,
    SET_MESSAGE,
} from "./types";

import Cookies from 'js-cookie';
import AuthService from "../services/auth-service";

export const signup = (email, password, confirmPassword) => async (dispatch) => {
    try {
        await AuthService.signup(email, password, confirmPassword);
        return Promise.resolve();
    }
    catch (error) {
        const message =
            (error.response &&
                error.response.data) ||
            error.response.data ||
            error.toString();

        dispatch({
            type: SET_MESSAGE,
            payload: message,
        });
        return Promise.reject();
    }
};

export const login = (email, password) => async (dispatch) => {
    try {
        const response = await AuthService.login(email, password);
        Cookies.set("user", JSON.stringify(response.data, { httpOnly: true }))
        dispatch({
            type: LOGIN_SUCCESS,
            payload: response.data,
        });
        return Promise.resolve();
    }
    catch (error) {
        const message =
            (error.response &&
                error.response.data) ||
            error.response.data ||
            error.toString();

        dispatch({
            type: LOGIN_FAIL,
        });

        dispatch({
            type: SET_MESSAGE,
            payload: message,
        });
        return Promise.reject();
    }
};

export const changePassword = (currentPassword, newPassword, confirmNewPassword) => async (dispatch) => {
    try {
        await AuthService.changePassword(currentPassword, newPassword, confirmNewPassword);
        return Promise.resolve();
    }
    catch (error) {
        const message =
            (error.response &&
                error.response.data) ||
            error.response.data ||
            error.toString();

        dispatch({
            type: SET_MESSAGE,
            payload: message,
        });
        return Promise.reject();
    }
}

export const logout = () => (dispatch) => {
    Cookies.remove("user");
    dispatch({
        type: LOGOUT,
    });
};