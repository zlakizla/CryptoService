  var url = "http://127.0.0.1:8080/crypto/";
  $(document).ready(function () {
        var Account1_seed = "2UiJ8Fte8bvuZSFjhdEtJ2etVvbirNRDTu8KEs9BFxch";
        var Account1_privateKey = "4XeFFL279quugYpvkqSPHwsK68jumG7C9CWz7QzSWJapjSB1FGiSDSawg65YZorRt2GbAP25gGv8ooduMxWpp7HD";
        var Account1_publicKey = "BHJAVuNsvcjWy6jaaF85HHYzr9Up9rA4BW3xseUBs9Un";

        var Account2_seed = "DjUm5c4rat2xx8uD5TgUeWf1HreM3WzwrVaE39WemCxY";
        var Account2_privateKey = "2mfVsVHpQ9jnwrpeCksjxUBrHMD2P8e1JN9VUdv5K8RBzfonS4EZDfAYxGh6afosj4uC5ryZJpLjizipEAy56E74";
        var Account2_publicKey = "217z8Wp4ztArt9qEzLhNgb4gErvnLQaqwmyxWo2DeZCA";

      var message = "test message for encrypt and decrypt";
        var encryptMessage = "CjM7CfrxdZRbrtGdWx2iPnWcsCbS8MH4vA4kc3jgCsgvgDVzGtJNmkweApeE6BZgGy";
        var signature = "26xAhHEhZ1kh4L9svvqb1RQFPgR2emHf592AchQywLrHPVfX8aLpwRUrS4gEg3XR2zUhYHE7d5FWbUrSo3Nni9K1";

      //var Account1_seed, Account1_privateKey, Account1_publicKey, Account2_seed, Account2_privateKey, Account2_publicKey, encryptMessage, signature;

      $('#seed1').text(Account1_seed);
      $('#seed2').text(Account2_seed);

      $("#publicSeed1").text(Account1_publicKey)
      $("#privateSeed1").text(Account1_privateKey)

      $("#message").text(message)

      GenerateSeed();
      GenerateKeyPair(Account2_seed);
      Encrypt(message, Account2_publicKey, Account1_privateKey);
      Decrypt(encryptMessage, Account1_publicKey, Account2_privateKey);
      Sign(encryptMessage, Account1_publicKey, Account1_privateKey);
      VerifySignature(encryptMessage, Account1_publicKey, signature);
  });

  function GenerateSeed() {

      $.ajax({
          url: url + "generateSeed",
          dataType: "json",
          type: "GET",
          success: function (d) {
              $("#seed").text(d.seed);
              console.log(d);
          }
      });
  }

  function GenerateKeyPair(seed) {
      $.ajax({
          url: url + "generateKeyPair/" + seed,
          crossDomain: true,
          type: "GET",
          success: function (d) {


              $("#publicSeed2").text(d.publicKey)
              $("#privateSeed2").text(d.privateKey)

              // this gen by  account 2

              console.log(d);
          }
      });
  }

  function Encrypt(ms, Acc2_public, Acc1_private) {
      $("#publicSeed2").text(Acc2_public)

      var data = "{\"message\": \"" + ms + "\",\"publicKey\":\"" + Acc2_public + "\",\"privateKey\":\"" + Acc1_private + "\"}";
      $.ajax({
          data: data,
          url: url + "encrypt",
          crossDomain: true,
          type: "POST",
          success: function (d) {
              $("#encrypt").text(d.encrypted)
              console.log(d);
          }
      });
  }

  function Decrypt(encMess, Acc1_public, Acc2_private) {
      $("#privateSeed2").text(Acc2_private)
      var data = "{\"message\": \"" + encMess + "\", \"publicKey\":\"" + Acc1_public + "\",\"privateKey\":\"" + Acc2_private + "\"}";
      $.ajax({
          data: data,
          url: url + "decrypt",
          crossDomain: true,
          type: "POST",
          success: function (d) {
              $("#decrypt").text(d.decrypted)
              console.log(d);
          }
      });
  }

  function Sign(encMess, Acc1_public, Acc1_private) {

      var data = "{\"message\": \"" + encMess + "\", \"publicKey\":\"" + Acc1_public + "\",\"privateKey\":\"" + Acc1_private + "\"}";
      $.ajax({
          data: data,
          url: url + "sign",
          crossDomain: true,
          type: "POST",
          success: function (d) {
              console.log(d);
              $("#sign").text(d.signature)
          }
      });

  }

  function VerifySignature(encMess, Acc1_public, sign) {
      var data = "{\"message\": \"" + encMess + "\", \"publicKey\":\"" + Acc1_public + "\",\"signature\":\"" + sign + "\"}";
      $.ajax({
          data: data,
          url: url + "verifySignature",
          crossDomain: true,
          type: "POST",
          success: function (d) {
              $("#verifySign").text(d.signatureVerify)
              console.log(d);
          }
      });

  }