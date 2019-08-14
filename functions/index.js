const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

// Listens for new likes added to /likes/:pushId and sends a push notification
exports.sendLikeNotification = functions.database.ref('/likes/{pushId}')
    .onCreate((snapshot, context) => {
        // Grab the current value of what was written to the Realtime Database.
        const like = snapshot.val();

        // Get the image liked
        admin.database().ref(`/images/${like.imageId}`).once("value", (snapshot) => {
            const image = snapshot.val();

            // get the user that liked the image
            const getUserLikedPromise = admin.database().ref(`/users/${like.userId}`).once('value');
            // get the user that posted the image
            const getUserImagePromise = admin.database().ref(`/users/${image.userId}`).once('value');

            return Promise.all([getUserLikedPromise, getUserImagePromise]).then(results => {
                const userLiked = results[0].val();
                const userImage = results[1].val();

                if (userLiked.uid === userImage.uid) {
                    return console.log("User liked his own image.");
                }

                if (userImage.token) {
                    // Notification details.
                    const payload = {
                        notification: {
                            title: 'You have a new like!',
                            body: `${userLiked.displayName} liked your image.`
                        }
                    };

                    console.log("Will send notification to user: " + userImage.uid);

                    return admin.messaging().sendToDevice(userImage.token, payload);
                }

                return console.log("No device token for user: " + user.uid);
            });
        }, (errorObject) => {
            return console.log("The read failed: " + errorObject.code);
        });
    });