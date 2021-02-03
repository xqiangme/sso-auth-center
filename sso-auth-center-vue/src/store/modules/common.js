const common = {
    state: {
        isCollapse: false
    },
    mutations: {
        // 折叠侧边栏
        TOGGLE_SIDEBAR(state) {
            state.isCollapse = !state.isCollapse
        }
    },
    actions: {
        toggleSidebar({ commit }) {
            commit('TOGGLE_SIDEBAR')
        }
    }
}

export default common