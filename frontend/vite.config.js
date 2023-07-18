import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

// function envVarNamesToDefinitions(envVars) {
//   const defs = {}
//   for (const envVar of envVars) {
//     // defs[`import.meta.env.${envVar}`] = JSON.stringify(process.env[envVar])
//     // defs[`process.env.${envVar}`] = JSON.stringify(process.env[envVar])
//   }
//   // defs[`process.env.${envVar}`] = JSON.stringify(process.env[envVar])
//   // console.log(`Environment variables:`) ////
//   // console.log(defs) ////
//   return defs
// }  

// function allEnvs() {
//   const env = lodefsadEnv(mode, process.cwd(), '')
//   const env = process.env
//   return { 'process.env': env }
// }

// https://vitejs.dev/config/
// export default defineConfig({
//   plugins: [react()],
//   define: {
//     ...envVarNamesToDefinitions([
//       'SERVER_PORT',      'SERVER_HOST',       'SERVER_PROTOCOL', 
//       'HCAPTCHA_SITEKEY', 'RECAPTCHA_SITEKEY', 'REQUIRE_CAPTCHA'
//     ]),
//     // ...allEnvs(),
//   }
// })

// const f = ({ mode }) => {
//   // process.env = {
//   //   ...process.env, 
//   //   loadEnv(mode) 
//   // }
//   process.env = {
//     ...process.env,
//     loadEnv(mode)
//   }

//   return defineConfig({
//     plugins: [react()],
    
//   })
// }

// export default defineConfig(({ mode }) => {
//   // process.env = Object.assign(process.env, loadEnv(mode, process.cwd(), ''))
//   const env = loadEnv(mode, process?.cwd(), '')

//   return {
//     plugins: [react()],
//     define: {
//       // 'import.meta.env': env,
//       'process.env': env,
//     }
//   }
// })

export default () => {
  return defineConfig({
     plugins: [react()],
  })
}