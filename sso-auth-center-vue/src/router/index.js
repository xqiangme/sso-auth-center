import Vue from 'vue'
import Router from 'vue-router'
/*  布局组件 Layout */
import Layout from '@/layout/index'

Vue.use(Router)

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/myPlatform/index',
    hidden: true
  },
  {
    path: '/user/profile',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'profile',
        component: () => import('@/views/sso/user/profile/index'),
        meta: { title: '个人详情', icon: '#' }
      }
    ]
  },
  {
    path: '/myPlatform',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'myPlatform',
        component: () => import('@/views/sso/platform/platformMy'),
        meta: { title: '我的平台', icon: '#' }
      }
    ]
  },
  {
    path: '/platformSort',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'platformSort',
        component: () => import('@/views/sso/platform/platformSort'),
        meta: { title: '平台排序', icon: '#' },
        hidden: true
      }
    ]
  },
  {
    path: '/platformMgmt',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'platformMgmt',
        component: () => import('@/views/sso/platform/platformMgmt'),
        meta: { title: '平台管理', icon: '#' }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'user',
        component: () => import('@/views/sso/user/index'),
        meta: { title: '用户管理', icon: 'dept' }
      }
    ]
  },
  {
    path: '/dept',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'dept',
        component: () => import('@/views/sso/dept/index'),
        meta: { title: '部门管理', icon: 'dept' }
      }
    ]
  },
  {
    path: '/menu',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'menu',
        component: () => import('@/views/sso/menu/index'),
        meta: { title: '菜单管理', icon: 'menu' }
      }
    ]
  },
  {
    path: '/role',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'role',
        component: () => import('@/views/sso/role/index'),
        meta: { title: '角色管理', icon: 'dept' }
      }
    ]
  },
  {
    path: '/userRole',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'userRole',
        component: () => import('@/views/sso/role/userRoleIndex'),
        meta: { title: '角色用户', icon: 'dept' },
        hidden: true
      }
    ]
  },
  {
    path: '/systemMgmt',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'systemMgmt',
        component: () => import('@/views/sso/platform/systemMgmtIndex'),
        meta: { title: '系统管理', icon: '' },
        hidden: true
      }
    ]
  },
  {
    path: '/platformMgmt',
    name: 'PlatformMgmtName',
    component: Layout,
    children: [
      {
        path: 'platformDetail',
        name: 'PlatformMgmtDetail',
        component: () => import('@/views/sso/platform/platformDetail'),
        meta: { title: 'platformMgmt', icon: 'platformMgmt' },
        hidden: true
      }
    ]
  },
  {
    path: '/log',
    name: 'log',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'logIndex',
        component: () => import('@/views/sso/log/index'),
        meta: { title: '日志', icon: '' },
        hidden: true
      }
    ]
  },
  {
    path: '/userOnline',
    name: 'userOnline',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'userOnline',
        component: () => import('@/views/sso/user/online/index'),
        meta: { title: '在线用户', icon: '' },
        hidden: true
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
