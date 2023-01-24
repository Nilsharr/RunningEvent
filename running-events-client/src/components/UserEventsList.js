import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Modal, Button } from "react-bootstrap";

import { getUserEvents, leaveEvent } from "../actions/events";
import Map from "./Map/Map"

const UserEventsList = () => {
    const [currentEvent, setCurrentEvent] = useState(null);
    const [currentIndex, setCurrentIndex] = useState(-1);
    const [showMapModal, setShowMapModal] = useState(false);

    const events = useSelector(state => state.events);
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(getUserEvents());
    }, [dispatch]);

    const setActiveEvent = (event, index) => {
        setCurrentEvent(event);
        setCurrentIndex(index);
    };

    const leaveCurrentEvent = () => {
        dispatch(leaveEvent(currentEvent.id))
            .then(() => {
                window.location.reload();
            })
            .catch(e => {
                console.log(e);
            });
    }

    return (
        <div className="list row">
            <div className="col-md-6">
                <h4>My Events</h4>

                <ul className="list-group">
                    {events &&
                        events.map((event, index) => (
                            <li
                                className={
                                    "list-group-item " + (index === currentIndex ? "active" : "")
                                }
                                onClick={() => setActiveEvent(event, index)}
                                key={index}
                            >
                                {event.name}
                            </li>
                        ))}
                </ul>

            </div>
            <div className="col-md-6">
                {currentEvent ? (
                    <div>
                        <h4>Event</h4>
                        <div>
                            <label>
                                <strong>Name:</strong>
                            </label>{" "}
                            {currentEvent.name}
                        </div>
                        <div>
                            <label>
                                <strong>Details:</strong>
                            </label>{" "}
                            {currentEvent.details}
                        </div>
                        <div>
                            <label>
                                <strong>Address:</strong>
                            </label>{" "}
                            {`${currentEvent.address.country}, ${currentEvent.address.city}, ${currentEvent.address.street}`}
                        </div>
                        <div>
                            <label>
                                <strong>Date:</strong>
                            </label>{" "}
                            {new Date(currentEvent.date).toUTCString()}
                        </div>
                        <div>
                            <label>
                                <strong>Maximum number of participants:</strong>
                            </label>{" "}
                            {currentEvent.maxParticipants}
                        </div>
                        <div>
                            <label>
                                <strong>Current number of participants:</strong>
                            </label>{" "}
                            {currentEvent.participants.length}
                        </div>
                        <div className="mt-3">
                            <Button variant="primary" onClick={() => setShowMapModal(true)}>
                                Show route
                            </Button>

                            <Modal show={showMapModal} centered={true} animation={false} size="xl" onHide={() => setShowMapModal(false)}>
                                <Modal.Header closeButton><b>Route</b></Modal.Header>
                                <Map event={currentEvent} canSetRoute={false} />
                            </Modal>
                        </div>
                        <div className="mt-3" >
                            <button className="btn btn-danger" type="button"
                                onClick={leaveCurrentEvent}>
                                Leave
                            </button>
                        </div>
                    </div>
                ) : (
                    <div>
                        <br />
                        <p>Click on an Event</p>
                    </div>
                )
                }
            </div >
        </div >
    );
};

export default UserEventsList;