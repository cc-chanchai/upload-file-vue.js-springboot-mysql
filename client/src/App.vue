<template>
  <div id="App">
    <div class="container" style="width:600px">
      <div style="margin: 20px">
        <h4>Vue.js Spring-Boot Upload File</h4>
      </div>
      <UploadFile/>
    </div>
  </div>
</template>

<script>
import UploadFile from './components/UploadFile'
export default {
  name:"App",
  components: {
    UploadFile
  }
}
</script>