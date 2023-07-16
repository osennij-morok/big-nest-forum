const config = {
  serverPort: Number(env('SERVER_PORT')),
  serverHost: env('SERVER_HOST'),
  serverProtocol: env('SERVER_PROTOCOL'),
  allowUsersAuthentication: true,
  hcaptchaSitekey: env('HCAPTCHA_SITE_KEY'),
  recaptchaSitekey: env('RECAPTCHA_SITE_KEY'),
  requireCaptcha: env('REQUIRE_CAPTCHA'),
  environment1: import.meta.env,
  // environment2: process?.env,
}

export default {
  ...config,
  serverUrl: `${config.serverProtocol}://${config.serverHost}:${config.serverPort}`,
}

function env(key) {
  return import.meta.env[key] || import.meta.env[`VITE_${key}`]
}
