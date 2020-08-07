<template>
    <div>
        <div v-if="currentFile" class="progress">
            <div
                class="progress-bar progress-bar-info progress-bar-striped"
                role="progressbar"
                :aria-valuenow="progress"
                aria-valuemin="0"
                aria-valuemax="100"
                :style="{ width: progress + '%' }"    
            >
                {{progress}}%
            </div>
        </div>
        <label class="btn btn-defualt">
            <input type="file" ref="file" @change="selectFile" />
        </label>
        <button class="btn btn-success" :disabled=!selectedFiles @click="upload">upload</button>
        
        <div v-if='message !== ""' class="alert alert-danger" role="alert">{{ message }}</div>
        
        <div class="card">
            <div class="card-header">List of Files</div>
            <ul class="list-group list-group=flush">
                <li
                    class="list-group-item"
                    v-for="(file,i) in fileInfos"
                    :key="i"
                >
                    <a :href="file.url">{{ file.name }}</a>
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
import UploadFileService from '../services/UploadFileService'

export default {
    name:"uploadFile",
    data(){
        return{
            selectedFiles: undefined,
            currentFile: undefined,
            progress: 0,
            message: "",
            fileInfos:[]
        }
    },    

    methods:{
        selectFile(){
            this.selectedFiles = this.$refs.file.files
        },

        upload(){
            this.progress = 0

            this.currentFile = this.selectedFiles[0]
            UploadFileService.upload(this.currentFile, event => {
                this.progress = Math.round((100 * event.loaded) / event.total)
            }).then( response => {
                this.message = response.data.message
                return UploadFileService.getFile();
            }).then(files => {
                this.fileInfos = files.data
            }).catch( () => {
                this.progress = 0
                this.message = "Could not upload file!"
                this.currentFile = undefined
            });

            this.selectedFiles = undefined
        }

    },
    mounted(){
        UploadFileService.getFile()
        .then(response => {
            this.fileInfos = response.data
        })
    }
}
</script>