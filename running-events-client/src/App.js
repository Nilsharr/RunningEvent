import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Router, Switch, Route } from "react-router-dom";
import { Container, Nav, Navbar, NavDropdown } from 'react-bootstrap';
import { PrivateRoute } from "./components/PrivateRoute";
import { AdminRoute } from "./components/AdminRoute";

import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import Signup from "./components/Signup";
import Login from "./components/Login";
import ChangePassword from "./components/ChangePassword"
import EditEvent from "./components/EditEvent";
import EventsList from "./components/EventsList";
import AddEvent from "./components/AddEvent";
import UserEventsList from "./components/UserEventsList"
import PageNotFound from "./components/PageNotFound"

import { logout } from "./actions/auth";
import { clearMessage } from "./actions/message";
import AuthService from './services/auth-service';
import Cookies from 'js-cookie';

import { history } from "./helpers/history";

const App = () => {
  const { isLoggedIn, user } = useSelector(state => state.auth);
  const dispatch = useDispatch();

  useEffect(() => {
    const verifyToken = () => {
      const token = user ? user.token : {};
      if (token !== null) {
        AuthService.verifyToken(token)
          .then(response => {
            if (!response.data.isValid) {
              dispatch(logout());
              Cookies.remove("user");
            }
          })
          .catch(e => {
            console.log(e);
          });
      }
    }
    verifyToken();
  }, [dispatch, user]);

  useEffect(() => {
    history.listen((location) => {
      dispatch(clearMessage());
    });
  }, [dispatch]);

  const logOut = () => {
    dispatch(logout());
  };

  return (
    <Router history={history}>

      <Navbar bg="dark" variant="dark" expand="lg">
        <Container>
          <Navbar.Brand href="/">Events</Navbar.Brand>
          <Navbar.Toggle aria-controls="navbar-nav" />
          <Navbar.Collapse>
            {isLoggedIn && user.admin && (
              <Nav className="me-auto">
                <Nav.Link href="/add-event">Add event</Nav.Link>
              </Nav>
            )}
            {isLoggedIn && (
              <Nav className="ml-auto">
                <NavDropdown variant="dark" title={user.username}>
                  {!user.admin && <NavDropdown.Item href="/user/events">My events</NavDropdown.Item>}
                  <NavDropdown.Item href="/change-password">Change password</NavDropdown.Item>
                  <NavDropdown.Item onClick={logOut}>Log out</NavDropdown.Item>
                </NavDropdown>
              </Nav>
            )}
          </Navbar.Collapse>
        </Container>
      </Navbar>

      <div className="container mt-3">
        <Switch>
          <PrivateRoute exact path={["/", "/events"]} component={EventsList} />
          <AdminRoute exact path="/add-event" component={AddEvent} />
          <AdminRoute exact path="/events/:id" component={EditEvent} />
          <Route exact path="/signup" component={Signup} />
          <Route exact path="/login" component={Login} />
          <PrivateRoute exact path="/change-password" component={ChangePassword} />
          <PrivateRoute exact path="/user/events" component={UserEventsList} />
          <Route path="*" component={PageNotFound} />
        </Switch>
      </div>
    </Router>
  );
};

export default App;