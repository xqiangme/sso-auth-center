<template>
  <div>
    <el-container>
      <el-aside width="auto">
        <logo v-if="showLogo" :collapse="collapseFlag"/>
        <el-menu v-if="isLeftMeuAlive" :default-active="leftMenuAlive" active-text-color="#333"
                 :collapse="collapseFlag" router>
          <el-menu-item v-for="(item,i) in leftMenuList" :key="i" :index="item.path">
            <svg-icon :iconClass="item.icon" aria-hidden="true" class="el-input__icon"
                      style="margin-right: 5px" v-on="$listeners"/>
            <span slot="title">{{ item.menuName }}</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-container>
        <div v-if="leftMenuList.length>0" style="position: absolute;margin-top: 20px;">
          <a class="el-icon-s-fold" style="color: white" v-if="!collapseFlag" @click="collapseClose"></a>
          <a class="el-icon-s-unfold" style="color: white" v-if="collapseFlag" @click="collapseOpen"></a>
        </div>
        <el-header>
          <div class="top-menu">
            <el-menu :default-active="activeIndex"
                     class="el-menu-top"
                     mode="horizontal"
                     @select="handleSelect"
                     background-color="#0C2740"
                     text-color="#fff"
                     active-text-color="#e9eaec">
              <el-menu-item v-for="(item,i) in headMenuList" :key="i" :index="item.path">
                {{ item.menuName }}
              </el-menu-item>
            </el-menu>
          </div>
          <div class="header-right">
            <div class="btn-fullscreen" @click="handleFullScreen" style="color: white">
              <el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
                <i class="el-icon-rank"></i>
              </el-tooltip>
            </div>
            <el-dropdown>
                  <span class="el-dropdown-link">
                      <img class="user-img" :src="avatar" alt="">
                      <span class="user-text" style="color: white;font-weight: bold;margin-left: 5px">{{ name }}</span>
                  </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="profileIndex">
                  <span>个人中心</span>
                </el-dropdown-item>
                <el-dropdown-item divided @click.native="logout">
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>
        <el-main :class="{'main-body-left':mainBodyLeftIsActive}">
          <transition name="fade-transform" mode="out-in">
            <router-view></router-view>
          </transition>
        </el-main>
        <!-- <el-footer>Footer</el-footer>-->
      </el-container>
    </el-container>
  </div>
</template>
<script>
import store from '@/store'
import Logo from '@/layout/Logo'
import variables from '@/styles/variables.scss'
import { mapGetters } from 'vuex'
import { isExternal } from '@/utils/validate'

export default {
  components: { Logo },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'name'
    ]),
    routes() {
      return this.$router.options.routes
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  },
  //点击页面侧边栏重载功能的实现
  //（该处提供了提供给后代组件的数据/方法）
  provide() {
    return {
      reloadLeftMenu: this.reloadLeftMenu
    }
  },
  data() {
    return {
      isLeftMeuAlive: true,
      mainBodyLeftIsActive: true,
      logoImg: null,
      testP: this.$route.query.testP,
      activeIndex: '',
      collapseFlag: false,
      elAsideClass: { 'width': '200px' },
      fullscreen: false,
      headMenuList: [],
      headMenuSet: [],
      leftMenuList: [],
      leftMenuSet: [],
      //左侧菜单默认选中项
      leftMenuAlive: ''
    }
  },
  created() {
    this.logoImg = require('@/assets/image/profile.jpg')
    if (null != store.getters.menuList && store.getters.menuList.length > 0) {
      //头部菜单
      this.headMenuList = store.getters.menuList
    }
    this.headMenuSet = new Set([])
    for (let i in this.headMenuList) {
      this.headMenuSet.add(this.headMenuList[i].path)
    }
    this.leftMenuList = []
    this.restActiveIndex()
  },
  methods: {
    //左侧二级菜单载入
    reloadLeftMenu() {
      if (this.headMenuSet.has(this.$route.path) && this.activeIndex !== this.$route.path) {
        this.activeIndex = this.$route.path
      }
      if (null == store.getters.menuMap || store.getters.menuMap.size <= 0) {
        return
      }

      const menu = store.getters.menuMap.get(this.activeIndex)
      if (menu != null) {
        if (this.leftMenuList.length > 0) {
            this.leftMenuAlive = this.$route.path
          return
        }
        if (menu.children.length > 0) {
          this.leftMenuList = menu.children
          this.handleReloadLeftMenu()
        }
      } else {
        this.restActiveIndex()
        this.leftMenuList = []
        this.mainBodyLeftIsActive = true
      }
    },
    restActiveIndex() {
      if (this.activeIndex === this.$route.path) {
        return
      }
      if (this.headMenuSet.has(this.$route.path) && this.activeIndex !== this.$route.path) {
        this.activeIndex = this.$route.path
      } else {
        const activeIndex = sessionStorage.getItem('activeIndex')
        if (activeIndex !== '' && activeIndex !== undefined && activeIndex !== null) {
          this.activeIndex = activeIndex
        }
      }
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.leftMenuList = []
        this.mainBodyLeftIsActive = true
        sessionStorage.removeItem('activeIndex')
        this.$store.dispatch('user/LogOut').then(() => {
          location.href = '/'
        })
      })
    },
    handleCollapseTrue() {
      this.collapseFlag = true
    },
    handleSelect(key, keyPath) {
      if (key === '' || key === null) {
        return
      }
      //如果是外链
      if (isExternal(key)) {
        const lastActiveIndex = sessionStorage.getItem('activeIndex')
        //重新定位到上一次访问
        if (null != lastActiveIndex) {
          this.$router.push(lastActiveIndex)
          //新页面打开外链
          window.open(key)
          //重新刷新页面
          window.location.reload()
        }
        //新页面打开外链
        window.open(key)
        return
      }
      this.activeIndex = key
      this.mainBodyLeftIsActive = true
      sessionStorage.setItem('activeIndex', key)
      this.leftMenuList = []
      this.$router.push(key)
    },
    collapseStatus() {
      this.collapseFlag = !this.collapseFlag
    },
    collapseOpen() {
      this.collapseFlag = false
    },
    collapseClose() {
      this.collapseFlag = true
    },
    handleReloadLeftMenu() {
      this.leftMenuAlive = this.$route.path
      this.mainBodyLeftIsActive = false
      this.isLeftMeuAlive = false
      this.$nextTick(function() {
        this.isLeftMeuAlive = true
      })
    },
    //个人中心
    profileIndex() {
      this.leftMenuList = []
      this.mainBodyLeftIsActive = true
      sessionStorage.removeItem('activeIndex')
      this.$router.push('/user/profile/index')
    },
    // 全屏事件
    handleFullScreen() {
      let element = document.documentElement
      if (this.fullscreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen()
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen()
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen()
        }
      } else {
        if (element.requestFullscreen) {
          element.requestFullscreen()
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen()
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen()
        } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen()
        }
      }
      this.fullscreen = !this.fullscreen
    }
  }
}

</script>

<style scoped>

.el-header {
  background-color: #0C2740;
  color: #333;
  text-align: center;
  font-size: 16px;
  font-weight: bolder;
  line-height: 50px;
}

.el-footer {
  background-color: #ffffff;
  color: #333;
  text-align: center;
  line-height: 50px;
}

.el-aside {
  background-color: #ffffff;
  color: #333;
  /*text-align: center;*/
  /*line-height: 60px;*/
}

.el-main {
  background-color: #ffffff;
  color: #333;
  /*text-align: center;*/
  /*line-height: 60px;*/
}

.main-body-left {
  margin-left: -30px;
}

.el-header .el-menu-item.is-active {
  background-color: rgb(184, 82, 35) !important;
}

.el-aside .el-menu-item.is-active {
  background-color: rgb(232, 242, 255) !important;
  font-weight: bolder;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>

<style scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>

<style lang="scss" scoped>
.header-right {
  position: absolute;
  top: 5px;
  right: 30px;
  height: 50px;
  line-height: 50px;

  .btn-fullscreen {
    display: inline-block;
    transform: rotate(45deg);
    margin-right: 20px;
    font-size: 24px;
    vertical-align: top;
  }

  .el-dropdown-link {
    display: inline-block;
    height: 50px;
    line-height: 50px;
    padding: 0 5px;

    .user-img {
      width: 50px;
      height: 50px;
      border-radius: 30%;
      vertical-align: top;
    }

    .user-text {
      display: inline-block;
      padding-left: 5px;
      vertical-align: top;
    }
  }

  .el-dropdown-link:hover {
    background-color: #0C2740;
  }
}
</style>
