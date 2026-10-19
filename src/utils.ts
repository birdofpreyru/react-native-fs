import { Buffer } from 'buffer';

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

type ReadFileCommand = (path: string) => Promise<string>;

export type EncodingT = 'ascii' | 'base64' | 'utf8';
export type EncodingOptions = EncodingT | { encoding?: EncodingT };

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
 * Decodes Base64-encoded byte sequence `datum` into a string according to
 * the given `encoding` value:
 *  - For `utf8` (default) value of `encoding` it decodes `datum` into
 *    the original byte sequence, assumes it is UTF8 code, and further
 *    decodes it into UTF16 (regular JS) string; _i.e._ groups of 1-to-4
 *    original bytes turn into separate characters of the result string
 *    (from U+0000 to U+FFFF).
 *  - For `ascii` value of `encoding` it decodes `datum` into the original
 *    byte sequence, and returns it as UTF16 (regular JS) string without
 *    additional decoding (_i.e._ each original byte turns into a separate
 *    character of the result string, from U+0000 to U+00FF).
 *  - For `base64` value of `encoding` it just returns `datum` as is,
 *    effectively without any decoding.
 * @param datum Base64-encoded string.
 * @param encoding Encoding. Defaults `utf8`.
 * @return Result string.
 */
export function decode(b64: string, encoding: EncodingT = 'utf8'): string {
  if (encoding === 'base64') return b64;

  const buffer = Buffer.from(b64, 'base64');

  if (encoding === 'utf8') return buffer.toString('utf8');

  // Note: `ascii` in Buffer API would also unset the highest bit in each byte,
  // thus we really want to use `latin1` here.
  if (encoding === 'ascii') return buffer.toString('latin1');

  throw Error(`Invalid encoding type "${encoding}"`);
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
): Promise<string> {
  const b64: string = await command(normalizeFilePath(filepath));
  return decode(b64, toEncoding(encodingOrOptions));
}

export function toArrayBuffer(
  data: ArrayBuffer | string,
  encodingOrOptions?: EncodingOptions,
): ArrayBuffer {
  if (data instanceof ArrayBuffer) return data;
  return encode(data, toEncoding(encodingOrOptions));
}
