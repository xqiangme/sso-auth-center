import JSEncrypt from 'jsencrypt/bin/jsencrypt'

// RSA 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLX5ZZbSJ2xPozC0HXPYJrovGS\n' +
  'DHzIOHvqfzttK6kLgUHLnguMeHz5NdObvkOBR93p5BawBbgnzhUqDCDh2L3P1HNK\n' +
  'BzAh7gRoncHCyeSk47w2xL1cqeRPymhryMu9O4TVAwPYy0k5JOsoFyMpwnpJxOMf\n' +
  'GGTgNBSxmoPT4pO2+wIDAQAB'

const privateKey = 'MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMtfllltInbE+jML\n' +
  'Qdc9gmui8ZIMfMg4e+p/O20rqQuBQcueC4x4fPk105u+Q4FH3enkFrAFuCfOFSoM\n' +
  'IOHYvc/Uc0oHMCHuBGidwcLJ5KTjvDbEvVyp5E/KaGvIy707hNUDA9jLSTkk6ygX\n' +
  'IynCeknE4x8YZOA0FLGag9Pik7b7AgMBAAECgYEApiXIafSb9FYGZc8rkhHBS/hJ\n' +
  'zNyA0P48Vh5tyUmjCDAoNZY/rxn8V4ifARzWf3bcGCnsUw/JHFPjvH8+GeR9ZCkf\n' +
  'yLZXOADW5hu93ybjce42R2DGUu6BorEKdLK/BjiYGIvvfqk0xOre8GANnoXReLgo\n' +
  'KWyNQVy9QcrecjNtm+kCQQDqnfSVjzcoplqGGqFLarFt9OnBZITwQv6TvoPpwyOv\n' +
  'B5LDzAt4rVrmDo9vHSAx4ycCU7F53XUkhm0EmMGHoV8NAkEA3eipMG4TMoC/vxKz\n' +
  '8HsVxxb3JNs8tqh/eF0s5PCfDwsd792vOa5z6gMQ7ps7nT3vv8wVAKKJ4ui0iUtU\n' +
  'hUgsJwJBAJVvvIzSZrC2nKwUJC5UK+U8fPNYYjBii6JxU7Y+O+lNKpcZzSi1SlOE\n' +
  'bKm6ZHpCE+OwiTd07hswoBmwbnxENbUCQBNNuvIIkUjyZDu138tKmcFg4QzmuWhW\n' +
  'TghlapNbzypa2DbWfPiykUjJDX8EJ/Jswd9YXHdarE392j6bO/YAKj8CQQCBEZXS\n' +
  'XPC0JP8qe417W4GybWYcWnFqpTLwPEwsV0AfUBqzSbD2LPbaiZdk/+Oa3nYaLdOf\n' +
  'aCspsj2AUl7N8UZO'

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对数据进行加密
}

// 解密
export function decrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey) // 设置私钥
  return encryptor.decrypt(txt) // 对数据进行解密
}

