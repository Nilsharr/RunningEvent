import React, { useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Redirect, Link } from 'react-router-dom';

import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import { required, passwordLength } from "../helpers/validators"
import { signup } from "../actions/auth";

const Signup = (props) => {
    const form = useRef();
    const checkBtn = useRef();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [loading, setLoading] = useState(false);

    const { isLoggedIn } = useSelector(state => state.auth);
    const { message } = useSelector(state => state.message);

    const dispatch = useDispatch();

    const handleSignup = (e) => {
        e.preventDefault();
        setLoading(true);
        form.current.validateAll();

        if (checkBtn.current.context._errors.length === 0) {
            dispatch(signup(email, password, confirmPassword))
                .then(() => {
                    props.history.push("/login");
                    // is this necessary?
                    window.location.reload();
                })
                .catch(() => {
                    setLoading(false);
                });
        } else {
            setLoading(false);
        }
    };

    if (isLoggedIn) {
        return <Redirect to="/events" />;
    }

    return (
        <div className="col-md-12">
            <div className="card card-container">
                <img
                    src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
                    alt="profile-img"
                    className="profile-img-card"
                />

                <Form onSubmit={handleSignup} ref={form}>
                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <Input
                            type="text"
                            className="form-control"
                            name="email"
                            maxLength={50}
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            validations={[required]}
                        />
                        {message && message.authError && message.authError.loginInvalid && (
                            <div className="form-group">
                                <div className="alert alert-danger" role="alert">
                                    {message.authError.loginInvalid}
                                </div>
                            </div>
                        )}
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <Input
                            type="password"
                            className="form-control"
                            name="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            validations={[required, passwordLength]}
                        />
                        {message && message.authError && message.authError.passwordInvalid && (
                            <div className="form-group">
                                <div className="alert alert-danger" role="alert">
                                    {message.authError.passwordInvalid}
                                </div>
                            </div>
                        )}
                    </div>

                    <div className="form-group">
                        <label htmlFor="confirmPassword">Confirm Password</label>
                        <Input
                            type="password"
                            className="form-control"
                            name="confirmPassword"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            validations={[required, passwordLength]}
                        />
                    </div>

                    <div className="form-group">
                        <button className="btn btn-primary btn-block" disabled={loading}>
                            {loading && (
                                <span className="spinner-border spinner-border-sm"></span>
                            )}
                            <span>Sign up</span>
                        </button>
                    </div>

                    <div className="form-group">
                        <Link to="login">Have an account? Login.</Link>
                    </div>

                    {message && message.error && (
                        <div className="form-group">
                            <div className="alert alert-danger" role="alert">
                                {message.error}
                            </div>
                        </div>
                    )}
                    <CheckButton style={{ display: "none" }} ref={checkBtn} />
                </Form>
            </div>
        </div>
    );
};

export default Signup;