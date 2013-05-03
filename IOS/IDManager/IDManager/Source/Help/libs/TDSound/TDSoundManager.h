//
//  SoundManager.h
//  TrieuPhuOnline
//
//  Created by user on 5/3/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AudioToolbox/AudioToolbox.h>
#import <AVFoundation/AVFoundation.h>

@interface TDSoundManager : NSObject{
    AVAudioPlayer *_bgMusic;
    AVAudioPlayer *_longEffect;
    id _targetLongEffect;
    SEL _selectorLongEffect;
    id _objectLongEffect;
}
-(void)playBackGroundMusic:(NSString *)path loop:(BOOL)loop;
-(void)stopBackGroundMusic;
-(void)playBackGroundMusic;
-(void)pauseBackGroundMusic;
-(void)resumeBackGroundMusic;
+(TDSoundManager *)shareSMN;
-(void)updateVolumeBg;

-(void)playBackGroundMusic:(NSString *)path target:(id)target doWhenDone:(SEL)selector withObject:(id)object;

#pragma mark - long effect
-(void)cancelLongEffect;
-(void)playLongEffect:(NSString *)path target:(id)target doWhenDone:(SEL)selector withObject:(id)object;


-(void)playShortEffectWithPath:(NSString *)path;
+(void)playShortEffectWithFile:(NSString*)file;
@end
