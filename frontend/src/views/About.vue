<template>
  <div class="about">
    <CardsFilter/>
    <!-- <Cards/> -->
  </div>
</template>

<script>
import CardsFilter from'@/components/CardsFilter.vue'
// import Cards from '@/components/Cards.vue'

export default {
  name: 'About',
  components: {
    // Cards,
    CardsFilter
  }
}
</script>