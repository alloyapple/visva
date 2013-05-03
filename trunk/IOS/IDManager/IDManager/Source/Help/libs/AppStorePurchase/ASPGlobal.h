//
//  ASPGlobal.h
//  Game3012
//
//  Created by AnhVT on 11/5/12.
//
//

#import <Foundation/Foundation.h>

#if !defined(DEBUG) || DEBUG == 0
#define ASLOC(...) do {} while (0)
#define ASLOCINFO(...) do {} while (0)
#define ASLOCERROR(...) do {} while (0)

#elif DEBUG == 1
#define ASLOC(...) NSLog(__VA_ARGS__)
#define ASLOCERROR(...) NSLog(__VA_ARGS__)
#define ASLOCINFO(...) do {} while (0)

#elif DEBUG > 1
#define ASLOC(...) NSLog(__VA_ARGS__)
#define ASLOCERROR(...) NSLog(__VA_ARGS__)
#define ASLOCINFO(...) NSLog(__VA_ARGS__)
#endif // DEBUG

#define ASP_NOTIFICATION_PURCHASE_ERROR     @"kASPNotificationPurchaseError"
#define ASP_NOTIFICATION_PURCHASE_FINISH    @"kASPNotificationPurchaseFinish"

#define ASP_NOTIFICATION_NETWORK_ERROR      @"kASPNotificationNetworkError"
#define ASP_NOTIFICATION_NETWORK_FINISH     @"kASPNotificationNetworkFinish"
enum {
    TAG_NORMAL,
    TAG_NETWORK_ERROR,
    TAG_COMPLETED_PURCHASE,
    TAG_PURCHASE_ERROR,
    TAG_CONFIRM_RESEND_PURCHASE
};

@interface ASPGlobal : NSObject

@end
