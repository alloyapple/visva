//
//  TDConfig.h
//  MagicQuran
//
//  Created by tranduc on 11/19/12.
//
//

#ifndef MagicQuran_TDConfig_h
#define MagicQuran_TDConfig_h


#define TD_DEBUG 1
/*
 * if TD_DEBUG is not defined, or if it is 0 then
 *	all TDLOGXXX macros will be disabled
 *
 * if TD_DEBUG ==1 then:
 *		TDLOG() will be enabled
 *		TDLOGERROR() will be enabled
 *		TDLOGINFO()	will be disabled
 *
 * if TD_DEBUG==2 or higher then:
 *		TDLOG() will be enabled
 *		TDLOGERROR() will be enabled
 *		TDLOGINFO()	will be enabled
 */
#if !defined(TD_DEBUG) || TD_DEBUG == 0
#define TDLOG(...) do {} while (0)
#define TDLOGINFO(...) do {} while (0)
#define TDLOGERROR(...) do {} while (0)

#elif TD_DEBUG == 1
#define TDLOG(...) NSLog(__VA_ARGS__)
#define TDLOGERROR(...) NSLog(__VA_ARGS__)
#define TDLOGINFO(...) do {} while (0)

#elif TD_DEBUG > 1
#define TDLOG(...) NSLog(__VA_ARGS__)
#define TDLOGERROR(...) NSLog(__VA_ARGS__)
#define TDLOGINFO(...) NSLog(__VA_ARGS__)
#endif // TD_DEBUG

#define TD_ASSERT 1
#if TD_ASSERT==1
#define TDASSERT(condition, desc, ...) NSAssert((condition), (desc), __VA_ARGS__)
#else
#define TDASSERT(condition, desc, ...) do{}while(0);
#endif

#pragma mark - help
#define ccp(X,Y) CGPointMake((X),(Y))
#define TDLOGRect(str, f) TDLOG(@"%@ (x=%f, y=%f, w=%f, h=%f)", str, f.origin.x, f.origin.y, f.size.width, f.size.height)

#endif
