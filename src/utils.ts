import { Buffer } from 'buffer';

import RNFS from './NativeReactNativeFs';

/**
 * If given `path` starts with `file://` schema, it returns the path without
 * that schema; otherwise returns given `path` as is.
 * @param path
 * @returns
 */
export function normalizeFilePath(path: string): string {
  return path.startsWith('file://') ? path.slice(7) : path;
}

/** Valid encoding values to pass into Buffer methods. */
type BufferEncodingT = 'base64' | 'latin1' | 'utf8';

type ReadFileCommand = (path: string) => Promise<number>;

export type EncodingT = 'ArrayBuffer' | 'ascii' | 'base64' | 'utf8';

export type EncodingOptions<T extends EncodingT = EncodingT> =
  T | { encoding?: T };

/**
 * Reduces `encodingOrOptions` argument to encoding value,
 * which will be one of `ascii`, `base64`, or `utf8` (default).
 * @param encodingOrOptions
 * @returns
 */
export function toEncoding(encodingOrOptions?: EncodingOptions): EncodingT {
  const res =
    typeof encodingOrOptions === 'string'
      ? encodingOrOptions
      : encodingOrOptions?.encoding;
  return res || 'utf8';
}

/**
 * Decodes binary ArrayBuffer into a string, according to the given `encoding`
 * value:
 *
 *  - For `utf8` (default) value of `encoding` it assumes `data` are an UTF8
 *    encoded string, and they are decoded into UTF16 (regular JS) string
 *    (i.e. groups of 1-to-4 original bytes turn into separate characters
 *    of the result string, within the U+0000 to U+FFFF range).
 *
 *  - For `ascii` value of `encoding` each input byte becomes a separate
 *    character of the result string, within the U+0000 to U+00FF range.
 *
 *  - For `base64` value of `encoding` the input bytes are encoded into Base64
 *    encoded string.
 */
export function decode(
  data: ArrayBuffer,
  encoding: EncodingT = 'utf8',
): string {
  if (encoding === 'ArrayBuffer') throw Error('Bad encoding');
  const e: BufferEncodingT = encoding === 'ascii' ? 'latin1' : encoding;
  return Buffer.from(data).toString(e);
}

/**
 * Encodes UTF16 (regular JS) `data` string into binary ArrayBuffer according
 * to the given `encoding` mode:
 *
 *  - For `utf8` (default) value of `encoding` each original string character
 *    (from U+0000 to U+FFFF range) is transformed into a sequence of 1-to-4
 *    one-byte characters (from U+0000 to U+00FF range).
 *
 *  - For `ascii` value of `encoding` each character of the original string is
 *    supposed to be within U+0000 to U+00FF range, and it becomes a one-byte
 *    character of the result array. The function throws if any input character
 *    is outside the valid U+0000 to U+00FF range.
 *
 *  - For `base64` value of `encoding` it transforms the given Base64 string
 *    into the original byte array.
 */
export function encode(
  data: string,
  encoding: EncodingT = 'utf8',
): ArrayBuffer {
  if (encoding === 'ArrayBuffer') throw Error('Bad encoding');
  const e: BufferEncodingT = encoding === 'ascii' ? 'latin1' : encoding;
  return Buffer.from(data, e).buffer;
}

/**
 * Generic function used by readFile and readFileAssets
 */
export async function readFileGeneric(
  filepath: string,
  encodingOrOptions: EncodingOptions | undefined,
  command: ReadFileCommand,
): Promise<ArrayBuffer | string> {
  const id = await command(normalizeFilePath(filepath));
  const bytes = RNFS.getArrayBuffer(id);
  const encoding = toEncoding(encodingOrOptions);
  return encoding === 'ArrayBuffer' ? bytes : decode(bytes, encoding);
}

export function toArrayBuffer(
  data: ArrayBuffer | string,
  encodingOrOptions?: EncodingOptions,
): ArrayBuffer {
  if (data instanceof ArrayBuffer) return data;
  return encode(data, toEncoding(encodingOrOptions));
}
