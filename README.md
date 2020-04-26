# GooglePay (Backend independent)
The purpose of this repository to provide an example of Google Pay API without using a dedicated backend. 
Although the example makes use of the server side Java library but its adapted/enhanced for Android and 
provides a quick way to develop and test prototype for internal proof of concept or to demo to stakeholders 
before spending considerable effort to add this feature into your app.

In case you are wondering why didn't I use the Mobile SDK? The SDK is very limited in functionality and not very flexible.
In production environment you would go with service side solution (which is also recommended by the Google) and therefore, would like to develop prototype before involving backend teams and product owner. 
  
## Google Pay Transit pass prototype
In this repository, I have used Transit pass API as an example to add Transit Class and Transit Object. 
However, the example can be adapted/extended to use other Google Pay features such as Loyalty programs, Boarding passes etc.
You can learn more about Google Pay Passes API [here](https://developers.google.com/pay/passes/guides/overview/about/about-google-pay-api-for-passes).

## How to run this project
In order to run and make this project work, you would need to add the missing **access key** and **configurations**.
The configs are in **GooglePayConfig** and private key you'll have to provide in **assets/private_key.json**.
Once you have the missing parts of the puzzle, just clone this repository and run like a normal android project.

### How do I get missing config and private key?
The config file requires some basic information which you'll find with comment **// CHANGE ME <......>**.
Add the missing values according to your setup and you are good to go.

**Note:** I'm Assuming that you are already familiar with the basic requirements like [service account](https://developers.google.com/pay/passes/guides/get-started/basic-setup/get-access-to-rest-api) and 
you already have requested the access of you [Merchant account](https://developers.google.com/pay/passes/guides/get-started/basic-setup/get-access-to-rest-api#2-tie-your-service-account-to-your-google-pay-api-for-passes-account). 
If not, please follow them to get the required access. 

The main missing pieces are:
* Private key (which you will download as json in [this step](https://developers.google.com/pay/passes/guides/get-started/basic-setup/get-access-to-rest-api#register))  
* Service account email address
* Issuer Id
* Application name (your application package. although not necessary)

## References and resources
The library I used was shared officially by Google [here](https://github.com/google-pay/passes-rest-samples). 
I imported the Java library and adapted it for Android by converting the most of the code to Kotlin. Furthermore, the Base64 from the library didn't work with Android.
Therefore had to use different dependency and wrote a wrapper to make it work.  
     

