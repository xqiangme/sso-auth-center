<template>
  <span>
    <span style="margin-right: 1px"><slot></slot></span>
    <el-tooltip :content="content" :placement="placement">
      <i class="el-icon-question" style="cursor: pointer;"/>
    </el-tooltip>
  </span>
</template>
<script>
    export default {
        name: 'HelpHint',
        props: {
            placement: {
                default: 'top'
            },
            content: String,
        },
        data() {
            return {}
        },
    }
</script>

