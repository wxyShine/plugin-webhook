<script setup lang="ts">
import confetti from "canvas-confetti";
import {onMounted} from "vue";

onMounted(() => {
  confetti({
    particleCount: 100,
    spread: 70,
    origin: {y: 0.6, x: 0.58},
  });
});
</script>

<template>
  <section id="plugin-starter">
    <div class="wrapper">
      <span class="title"> 待开发！ </span>
      <span class="message">可配置webhook自选事件</span>
    </div>
  </section>
</template>

<style lang="scss" scoped>
#plugin-starter {
  height: 100vh;
  background-color: #f8fafc;
}

.wrapper {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  gap: 1.5rem;

  .title {
    font-weight: 700;
    font-size: 1.25rem;
    line-height: 1.75rem;
  }

  .message {
    font-size: 0.875rem;
    line-height: 1.25rem;
    color: #4b5563;
  }


  @media (min-width: 640px) {
    .docs {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }
  }
}
</style>
