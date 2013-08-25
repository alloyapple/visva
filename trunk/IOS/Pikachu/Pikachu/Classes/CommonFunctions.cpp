//
//  CommonFunctions.cpp
//  Pikachu
//
//  Created by tinvukhac on 8/25/13.
//
//

#include "cocos2d.h"
#include "CommonFunctions.h"

CCString* getPngPath(const char* path)
{
    ccLanguageType curLang = CCApplication::sharedApplication()->getCurrentLanguage();
    if (curLang == kLanguageChinese) {
        CCString* realPath = CCString::createWithFormat("%s", path);
        return realPath;
    } else {
        return CCString::createWithFormat("%s", path);
    }
}