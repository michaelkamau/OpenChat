import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript
//
// export const helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

admin.initializeApp();

const MESSAGES_TOPIC = "messages_topic";

export const sendNewMessageNotification = functions.database.ref("{newMessageId}")
    .onWrite((newMessageSnapshot, context) => {
        const message = newMessageSnapshot.after.val();
        const messageSender = message.sender.name;
        const profilePic = message.sender.profilePicUrlString;
        console.log("New Message text: ", message);

        const notificationPayload = {
            notification: {
                title: "New Message",
                body: `${messageSender} has sent a new message`,
                icon: profilePic
            }
        }

        return admin.messaging().sendToTopic(MESSAGES_TOPIC, notificationPayload)
            .then((response) => {
                console.log("Successfully sent the notification ", response);
            })
            .catch((error) => {
                console.error("Error in notification ", error);
            } );
    });
