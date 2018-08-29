# Crypto Service

* Js library: *"JsCryptoService"*

* Java source: *"src/main/java"*

* Test source: *"src/test/java"*

* Jar: *"build/libs"*

* Java docs: *"build/docs/javadoc"*

Ip address server and port set in file setting.json

By default 
* ip: 127.0.0.1

* port: 8181

## Generated seed

Generate a 32-byte random master key

Method: GET

Example request:

http://127.0.0.1:8181/crypto/generateSeed

Example response:

JSON format
```JSON
{"seed":"2UiJ8Fte8bvuZSFjhdEtJ2etVvbirNRDTu8KEs9BFxch"}
```
Seed in Base58 encoding

## Generate key pair

| Field | Type   | Description                      |
| ----- | ------ | -------------------------------- |
| seed  | String | master key in <br> encode Base58 |

Method: GET

http://127.0.0.1:8181/crypto/generateKeyPair/{seed}

Example request

http://127.0.0.1:8181/crypto/generateKeyPair/DjUm5c4rat2xx8uD5TgUeWf1HreM3WzwrVaE39WemCxY

Answer in JSON format. Keys in Base58 encoding.

Example response:
```JSON
{"publicKey":"BHJAVuNsvcjWy6jaaF85HHYzr9Up9rA4BW3xseUBs9Unw","privateKey":"4XeFFL279quugYpvkqSPHwsK68jumG7C9CWz7QzSWJapjSB1FGiSDSawg65YZorRt2GbAP25gGv8ooduMxWpp7HD"}
```
## Encrypt

| Field      | Type   | Description                                       |
| ---------- | ------ | ------------------------------------------------- |
| message    | String | The message you want to encrypt                   |
| publicKey  | String | Public key of the second party in Base58 encoding |
| privateKey | String | My private key in Base58 encoding                 |

Method post

Example request

http://127.0.0.1:8181/crypto/encrypt

In body

```JSON
{"message": "test message for encrypt and decrypt","publicKey":"217z8Wp4ztArt9qEzLhNgb4gErvnLQaqwmyxWo2DeZCA","privateKey":"4XeFFL279quugYpvkqSPHwsK68jumG7C9CWz7QzSWJapjSB1FGiSDSawg65YZorRt2GbAP25gGv8ooduMxWpp7HD"}
```

Example response:

JSON format. Contains encrypted message in Base58 encoding

```JSON
{"encrypted":"CjM7CfrxdZRbrtGdWx2iPnWcsCbS8MH4vA4kc3jgCsgvgDVzGtJNmkweApeE6BZgGy"}
```

## Decrypt

| Field      | Type   | Description                                         |
| ---------- | ------ | --------------------------------------------------- |
| message    | String | The message you want to dencrypt in Base58 encoding |
| publicKey  | String | Public key of the second party in Base58 encoding   |
| privateKey | String | My private key in Base58 encoding                   |

Method POST

Example request:

http://127.0.0.1:8181/crypto/decrypt

In body

```JSON
{"message":"CjM7CfrxdZRbrtGdWx2iPnWcsCbS8MH4vA4kc3jgCsgvgDVzGtJNmkweApeE6BZgGy","publicKey":"BHJAVuNsvcjWy6jaaF85HHYzr9Up9rA4BW3xseUBs9Un","privateKey":"2mfVsVHpQ9jnwrpeCksjxUBrHMD2P8e1JN9VUdv5K8RBzfonS4EZDfAYxGh6afosj4uC5ryZJpLjizipEAy56E74"}
```

Example response:

JSON format. Answer contatins decrypt message in UTF-8 encoding.

```JSON
{"decrypted":"test message for encrypt and decrypt"}
```

## Sign

| Field      | Type   | Description                                     |
| ---------- | ------ | ----------------------------------------------- |
| message    | String | The message you want to sign in Base58 encoding |
| publicKey  | String | My public key in Base58 encoding                |
| privateKey | String | My private key in Base58 encoding               |

Method POST

Example request:

http://127.0.0.1:8181/crypto/sign

In body

```JSON
{"message": "CjM7CfrxdZRbrtGdWx2iPnWcsCbS8MH4vA4kc3jgCsgvgDVzGtJNmkweApeE6BZgGy", "publicKey":"BHJAVuNsvcjWy6jaaF85HHYzr9Up9rA4BW3xseUBs9Un","privateKey":"4XeFFL279quugYpvkqSPHwsK68jumG7C9CWz7QzSWJapjSB1FGiSDSawg65YZorRt2GbAP25gGv8ooduMxWpp7HD"}
```

Example response:

JSON format. Answer contains sign in Base58 encoding.

```JSON
{"signature":"26xAhHEhZ1kh4L9svvqb1RQFPgR2emHf592AchQywLrHPVfX8aLpwRUrS4gEg3XR2zUhYHE7d5FWbUrSo3Nni9K1"}
```

## Verify signature

| Field     | Type   | Description                                       |
| --------- | ------ | ------------------------------------------------- |
| message   | String | The message you want to verify in Base58 encoding |
| sign      | String | Sign message                                      |
| publicKey | String | The public key of the person who signed           |

Method POST

Example request:

http://127.0.0.1:8181/crypto/verifySignature

```JSON
{"message": "CjM7CfrxdZRbrtGdWx2iPnWcsCbS8MH4vA4kc3jgCsgvgDVzGtJNmkweApeE6BZgGy","publicKey":"BHJAVuNsvcjWy6jaaF85HHYzr9Up9rA4BW3xseUBs9Un","signature":"26xAhHEhZ1kh4L9svvqb1RQFPgR2emHf592AchQywLrHPVfX8aLpwRUrS4gEg3XR2zUhYHE7d5FWbUrSo3Nni9K1"}
```

Example response:

JSON format. In case if the signature verification was successful the system will return the value signatureVerify: "true"

```JSON
{"signatureVerify":"true"}
```