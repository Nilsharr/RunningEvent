import serverInstance from "../server/server";
import { SortBy } from "../helpers/enums";
import { getUserFromCookie } from "../helpers/cookieHelper";

const user = getUserFromCookie();
const authorizationToken = user ? { Authorization: `Bearer ${user.token}` } : {};

const getAll = (options) => {
  options = options || {};
  const params = {
    page: options.page || 1,
    limit: options.limit || 100,
    sortBy: options.sortBy || SortBy.name,
    name: options.searchByName,
    country: options.country,
    city: options.city,
    dateStart: options.dateStart,
    dateEnd: options.dateEnd
  };

  return serverInstance.get("/events", { params, headers: authorizationToken });
};

const getUserEvents = () => {
  return serverInstance.get(`/users/events`, { headers: authorizationToken });
};

const get = id => {
  return serverInstance.get(`/events/${id}`, { headers: authorizationToken });
};

const create = data => {
  return serverInstance.post("/events", data, { headers: authorizationToken });
};

const update = (id, data) => {
  return serverInstance.put(`/events/${id}`, data, { headers: authorizationToken });
};

const remove = id => {
  return serverInstance.delete(`/events/${id}`, { headers: authorizationToken });
};

const joinEvent = id => {
  return serverInstance.post(`/events/${id}/join`, {}, { headers: authorizationToken });
}

const leaveEvent = id => {
  return serverInstance.delete(`/events/${id}/leave`, { headers: authorizationToken });
}

const eventService = {
  getAll,
  getUserEvents,
  get,
  create,
  update,
  remove,
  joinEvent,
  leaveEvent,
};

export default eventService;