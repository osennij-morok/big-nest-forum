const config = {
  serverPort: Number(env('SERVER_PORT')),
  serverHost: env('SERVER_HOST'),
  serverProtocol: env('SERVER_PROTOCOL'),
  allowUsersAuthentication: true,
  hcaptchaSitekey: env('HCAPTCHA_SITEKEY'),
  recaptchaSitekey: env('RECAPTCHA_SITEKEY'),
  requireCaptcha: env('REQUIRE_CAPTCHA'),
}

export default {
  ...config,
  serverUrl: `${config.serverProtocol}://${config.serverHost}:${config.serverPort}`,
}

function env(key) {
  return import.meta.env[key] || import.meta.env[`VITE_${key}`]
}
