import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';


// reference: https://github.com/firebase/functions-samples/blob/master/fcm-notifications/functions/index.js

admin.initializeApp();

const DEVICES_REG_TOKENS_REF = "/fcm_registration_tokens";
const MESSAGES_REF = "messages";

export const sendNewMessageNotification = functions.database.ref("/messages/{newMessageId}")
    .onWrite((newMessageSnapshot, context) => {
        // Get list of devices reg tokens
        const getDevicesRegTokensPromise = admin.database()
            .ref(`${DEVICES_REG_TOKENS_REF}`).once('value');

        return getDevicesRegTokensPromise.then((tokensSnapshot) => {

            if (!tokensSnapshot.hasChildren()) {
                return new Promise(() => {
                    console.warn("There are no device registrations tokens");
                })
            }

            // Construct Notification 
            const message = newMessageSnapshot.after.val();
            const messageSender = message.sender.name;
            const profilePic = message.sender.profilePicUrlString;
            console.log("New Message text: ", message);

            const notificationPayload = {
                notification: {
                    title: `${messageSender} has sent a new message`,
                    body: `${message.message}`,
                    icon: profilePic
                }
            }

            let tokens = [];
            let tokenKeys = Object.keys(tokensSnapshot.val());
            for (const key in tokensSnapshot.val()) {
                if (tokensSnapshot.val().hasOwnProperty(key)) {
                    const token = tokensSnapshot.val()[key];
                    tokens.push(token);
                }
            }
            console.log("Reg tokens: ", tokens);

            /// Send notification promise
            return admin.messaging().sendToDevice(tokens, notificationPayload);
        });
    });
