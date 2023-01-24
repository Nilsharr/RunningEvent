import { Route, Redirect } from 'react-router-dom';
import { useSelector } from "react-redux";

export { AdminRoute };

function AdminRoute({ component: Component, ...rest }) {
    const { isLoggedIn, user } = useSelector(state => state.auth);

    return (
        <Route {...rest} render={props => {
            if (!isLoggedIn) {
                return <Redirect to={{ pathname: '/login', state: { from: props.location } }} />
            }
            if (!user.admin) {
                return <Redirect to={{ pathname: '/', state: { from: props.location } }} />
            }
            return <Component {...props} />
        }} />
    );
}