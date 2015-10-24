/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stargame.ad.util.cache.image;

import com.stargame.ad.util.global.AppConfig;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 
 * 名称：AbImageBaseCache.java 描述：图片缓存
 * 
 * @author hxy
 * @version v1.0
 * @date：2015-6-11
 */
@SuppressLint("NewApi")
public class AbImageBaseCache implements AbImageCache {

	/** LruCache. */
	private static LruCache<String, Bitmap> mCache;

	/** AbImageCache 实例. */
	private static AbImageBaseCache mImageCache;

	/**
	 * 构造方法.
	 */
	public AbImageBaseCache() {
		super();
		int maxSize = AppConfig.MAX_CACHE_SIZE_INBYTES;
		mCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				try {
					return value.getRowBytes() * value.getHeight();
				} catch (Exception e) {
					// e.printStackTrace();
					return value.getByteCount();
				}
			}
		};
	}

	/**
	 * 
	 * 获取单例的AbImageCache.
	 * 
	 * @return
	 */
	public static AbImageBaseCache getInstance() {
		if (mImageCache == null) {
			mImageCache = new AbImageBaseCache();
		}
		return mImageCache;
	}

	/**
	 * 根据key获取缓存中的Bitmap.
	 * 
	 * @param cacheKey
	 *            the cache key
	 * @return the bitmap
	 */
	@Override
	public Bitmap getBitmap(String cacheKey) {
		return mCache.get(cacheKey);
	}

	/**
	 * 增加一个Bitmap到缓存中.
	 * 
	 * @param cacheKey
	 *            the cache key
	 * @param bitmap
	 *            the bitmap
	 */
	@Override
	public void putBitmap(String cacheKey, Bitmap bitmap) {
		mCache.put(cacheKey, bitmap);
	}

	/**
	 * 从缓存中删除一个Bitmap.
	 * 
	 * @param requestUrl
	 *            the request url
	 * @param maxWidth
	 *            the max width
	 * @param maxHeight
	 *            the max height
	 */
	@Override
	public void removeBitmap(String requestUrl, int maxWidth, int maxHeight) {
		mCache.remove(getCacheKey(requestUrl, maxWidth, maxHeight));
	}

	/**
	 * 获取用于缓存的Key.
	 * 
	 * @param requestUrl
	 *            the request url
	 * @param maxWidth
	 *            the max width
	 * @param maxHeight
	 *            the max height
	 * @return the cache key
	 */
	public String getCacheKey(String url, int maxWidth, int maxHeight) {
		return new StringBuilder(url.length() + 12).append("#W")
				.append(maxWidth).append("#H").append(maxHeight).append(url)
				.toString();
	}

	/**
	 * 释放所有缓存.
	 */
	public void clearBitmap() {
		mCache.evictAll();
	}

}
