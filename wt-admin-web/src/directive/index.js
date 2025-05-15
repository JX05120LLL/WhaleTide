import waves from './waves'

const directives = {
  waves
}

export default {
  install(Vue) {
    Object.keys(directives).forEach(key => {
      Vue.directive(key, directives[key])
    })
  },
  waves
} 