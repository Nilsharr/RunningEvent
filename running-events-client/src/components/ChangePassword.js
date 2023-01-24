import React, { useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";

import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import { required, passwordLength } from "../helpers/validators"
import { changePassword } from "../actions/auth";

const ChangePassword = (props) => {
    const form = useRef();
    const checkBtn = useRef();

    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmNewPassword, setConfirmNewPassword] = useState("");
    const [loading, setLoading] = useState(false);

    const { message } = useSelector(state => state.message);

    const dispatch = useDispatch();

    const handleChangePassword = (e) => {
        e.preventDefault();
        setLoading(true);
        form.current.validateAll();

        if (checkBtn.current.context._errors.length === 0) {
            dispatch(changePassword(currentPassword, newPassword, confirmNewPassword))
                .then(() => {
                    props.history.push("/events");
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

    return (
        <div className="col-md-12">
            <h1>Change Password</h1>
            <div className="card card-container">
                <Form onSubmit={handleChangePassword} ref={form}>
                    <div className="form-group">
                        <label htmlFor="currentPassword">Current password</label>
                        <Input
                            type="password"
                            className="form-control"
                            name="currentPassword"
                            value={currentPassword}
                            onChange={(e) => setCurrentPassword(e.target.value)}
                            validations={[required, passwordLength]}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="newPassword">New password</label>
                        <Input
                            type="password"
                            className="form-control"
                            name="newPassword"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
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
                        <label htmlFor="confirmNewPassword">Confirm new password</label>
                        <Input
                            type="password"
                            className="form-control"
                            name="confirmNewPassword"
                            value={confirmNewPassword}
                            onChange={(e) => setConfirmNewPassword(e.target.value)}
                            validations={[required, passwordLength]}
                        />
                    </div>

                    <div className="form-group">
                        <button className="btn btn-primary btn-block" disabled={loading}>
                            {loading && (
                                <span className="spinner-border spinner-border-sm"></span>
                            )}
                            <span>Change password</span>
                        </button>
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

export default ChangePassword;