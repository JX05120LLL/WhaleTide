const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  roles: state => state.user.roles,
  username: state => state.user.username,
  email: state => state.user.email,
  phone: state => state.user.phone,
  createTime: state => state.user.createTime,
  lastLoginTime: state => state.user.lastLoginTime,
  routes: state => state.permission ? state.permission.routes : [],
  permission_routes: state => state.permission ? state.permission.routes : []
}

export default getters