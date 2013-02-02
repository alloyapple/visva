//
//  TDDatabase.h
//  MagicQuran
//
//  Created by tranduc on 12/3/12.
//
//

#import <Foundation/Foundation.h>

@interface TDDatabase : NSObject
/* @param: NSString *file : file name in bundle
 * @param: NSString *type : file type
 * @return: NSString* : full path of file in bundle, nil if file is not exits
 * @comment: Get full path in mainbundle from file name and file type. Return nil if file is not exits
 */

+(NSString*)pathBundle:(NSString*)file type:(NSString*)type;
/* @param: NSString *file : file name in document
 * @return: NSString* : full path of file in document
 * @comment: Get full path in document folder
 */
+(NSString*)pathInDocument:(NSString*)file;

/* @param: NSString *sourceFile : Path file contains source folder
 * @param: NSString *dest: Path file save duplicate data.
 * @comment: copy data from path 1 to path 2
 *
 */
+(void)copyDataFrom:(NSString *)sourceFile to:(NSString *)dest;
+(BOOL)deleteFile:(NSString*)path;
@end
