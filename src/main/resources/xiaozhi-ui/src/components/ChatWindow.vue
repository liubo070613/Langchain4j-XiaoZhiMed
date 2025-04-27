<template>
  <div class="app-layout">
    <div class="sidebar">
      <div class="logo-section">
        <img src="@/assets/logo.png" alt="小智" width="160" height="160" />
        <span class="logo-text">小智（医疗版）</span>
      </div>
      <el-button class="new-chat-button" @click="newChat">
        <i class="fa-solid fa-plus"></i>
        &nbsp;新聊天
      </el-button>
    </div>
    <div class="main-content">
      <div class="chat-container">
        <div class="message-list" ref="messaggListRef">
          <div
            v-for="(message, index) in messages"
            :key="index"
            :class="
              message.isUser ? 'message user-message' : 'message bot-message'
            "
          >
            <!-- 会话图标 -->
            <i
              :class="
                message.isUser
                  ? 'fa-solid fa-user message-icon'
                  : 'fa-solid fa-robot message-icon'
              "
            ></i>
            <!-- 会话内容 -->
            <span>
              <span v-if="message.isUser" v-html="message.content"></span>
              <span v-else v-html="markdownToHtml(message.content)"></span>
              <!-- loading -->
              <span
                class="loading-dots"
                v-if="message.isThinking || message.isTyping"
              >
                <span class="dot"></span>
                <span class="dot"></span>
              </span>
            </span>
          </div>
        </div>
        <div class="input-container">
          <el-input
            v-model="inputMessage"
            placeholder="请输入消息"
            @keyup.enter="sendMessage"
          ></el-input>
          <el-button @click="sendMessage" :disabled="isSending" type="primary" class="send-button"
            >发送</el-button
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import axios from 'axios'
import { v4 as uuidv4 } from 'uuid'
import { marked } from 'marked' // 导入 marked 库

const messaggListRef = ref()
const isSending = ref(false)
const uuid = ref()
const inputMessage = ref('')
const messages = ref([])

onMounted(() => {
  initUUID()
  // 移除 setInterval，改用手动滚动
  watch(messages, () => scrollToBottom(), { deep: true })
  hello()
})

const scrollToBottom = () => {
  if (messaggListRef.value) {
    messaggListRef.value.scrollTop = messaggListRef.value.scrollHeight
  }
}

const hello = () => {
  sendRequest('你好')
}

// Markdown 转 HTML 函数
const markdownToHtml = (content) => {
  if (!content) return '';
  
  // 配置 marked 选项
  marked.setOptions({
    breaks: true, // 启用换行符转换为 <br>
    gfm: true,    // 启用 GitHub 风格的 Markdown
    sanitize: false // 允许 HTML 标签
  });
  
  try {
    return marked.parse(content);
  } catch (error) {
    console.error('Markdown 解析错误:', error);
    return content;
  }
}

const sendMessage = () => {
  if (inputMessage.value.trim()) {
    sendRequest(inputMessage.value.trim())
    inputMessage.value = ''
  }
}

const sendRequest = (message) => {
  isSending.value = true
  const userMsg = {
    isUser: true,
    content: message,
    isTyping: false,
    isThinking: false,
  }
  //第一条默认发送的用户消息”你好“不放入会话列表
  if(messages.value.length > 0){
    messages.value.push(userMsg)
  }


  // 添加机器人加载消息
  const botMsg = {
    isUser: false,
    content: '', // 增量填充
    isTyping: true, // 显示加载动画
    isThinking: false,
  }
  messages.value.push(botMsg)
  const lastMsg = messages.value[messages.value.length - 1]
  scrollToBottom()

  axios
    .post(
      '/api/xiaozhi/chat',
      { memoryId: uuid.value, message },
      {
        responseType: 'stream', // 必须为合法值 "text"
        onDownloadProgress: (e) => {
          const fullText = e.event.target.responseText // 累积的完整文本
          let newText = fullText.substring(lastMsg.content.length)
          lastMsg.content += newText //增量更新
          console.log(lastMsg)
          scrollToBottom() // 实时滚动
        },
      }
    )
    .then(() => {
      // 流结束后隐藏加载动画
      messages.value.at(-1).isTyping = false
      isSending.value = false
    })
    .catch((error) => {
      console.error('流式错误:', error)
      messages.value.at(-1).content = '请求失败，请重试'
      messages.value.at(-1).isTyping = false
      isSending.value = false
    })
}

// 初始化 UUID
const initUUID = () => {
  let storedUUID = localStorage.getItem('user_uuid')
  if (!storedUUID) {
    storedUUID = uuidToNumber(uuidv4())
    localStorage.setItem('user_uuid', storedUUID)
  }
  uuid.value = storedUUID
}

const uuidToNumber = (uuid) => {
  let number = 0
  for (let i = 0; i < uuid.length && i < 6; i++) {
    const hexValue = uuid[i]
    number = number * 16 + (parseInt(hexValue, 16) || 0)
  }
  return number % 1000000
}

// 转换特殊字符
const convertStreamOutput = (output) => {
  return output
    .replace(/\n/g, '<br>')
    .replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;')
    .replace(/&/g, '&amp;') // 新增转义，避免 HTML 注入
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
}

const newChat = () => {
  // 这里添加新会话的逻辑
  console.log('开始新会话')
  localStorage.removeItem('user_uuid')
  window.location.reload()
}

</script>
<style scoped>
.app-layout {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 200px;
  background-color: #f4f4f9;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo-text {
  font-size: 18px;
  font-weight: bold;
  margin-top: 10px;
}

.new-chat-button {
  width: 100%;
  margin-top: 20px;
}

.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  background-color: #fff;
  margin-bottom: 10px;
  display: flex;
  flex-direction: column;
}

.message {
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 4px;
  display: flex;
  /* align-items: center; */
}

.user-message {
  max-width: 70%;
  background-color: #e1f5fe;
  align-self: flex-end;
  flex-direction: row-reverse;
}

.bot-message {
  max-width: 100%;
  background-color: #f1f8e9;
  align-self: flex-start;
}

.message-icon {
  margin: 0 10px;
  font-size: 1.2em;
}

.loading-dots {
  padding-left: 5px;
}

.dot {
  display: inline-block;
  margin-left: 5px;
  width: 8px;
  height: 8px;
  background-color: #000000;
  border-radius: 50%;
  animation: pulse 1.2s infinite ease-in-out both;
}

.dot:nth-child(2) {
  animation-delay: -0.6s;
}

@keyframes pulse {
  0%,
  100% {
    transform: scale(0.6);
    opacity: 0.4;
  }

  50% {
    transform: scale(1);
    opacity: 1;
  }
}
.input-container {
  display: flex;
  /* margin-top: -10px;  */
}

.input-container .el-input {
  flex: 1;
  margin-right: 10px;
  margin-bottom: 30px;
  height: 50px;
}

/* 调整发送按钮样式和定位 */
.send-button {
  margin-top: 10px; /* 与输入框保持一致的底部间距 */
}

/* 媒体查询，当设备宽度小于等于 768px 时应用以下样式 */
@media (max-width: 768px) {
  .main-content {
    padding: 10px 0 10px 0;
  }
  .app-layout {
    flex-direction: column;
  }

  .sidebar {
    /* display: none; */
    width: 100%;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
  }

  .logo-section {
    flex-direction: row;
    align-items: center;
  }

  .logo-text {
    font-size: 20px;
  }

  .logo-section img {
    width: 40px;
    height: 40px;
  }

  .new-chat-button {
    margin-right: 30px;
    width: auto;
    margin-top: 5px;
  }
}

/* 媒体查询，当设备宽度大于 768px 时应用原来的样式 */
@media (min-width: 769px) {
  .main-content {
    padding: 0 0 10px 10px;
  }

  .app-layout {
    display: flex;
    height: 100vh;
  }

  .sidebar {
    width: 200px;
    background-color: #f4f4f9;
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .logo-section {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .logo-text {
    font-size: 18px;
    font-weight: bold;
    margin-top: 10px;
  }

  .new-chat-button {
    width: 100%;
    margin-top: 20px;
  }
}
</style>
