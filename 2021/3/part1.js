const input =
`00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010`;
const numberOfRows = input.split(/\n/).length;

// write input as bytes to wasm memory
const memory = new WebAssembly.Memory({initial: 1});
const encoder = new TextEncoder();
const encodedString = encoder.encode(input);
const i8 = new Uint8Array(memory.buffer);
i8.set(encodedString);

// pass memory and log function to wasm
const imports = {
    js: {
      mem: memory,
      // this has to be passed, as memory is page aligned (64k), so wasm script does not know where input ends
      numberOfRows: new WebAssembly.Global({value: "i32"}, numberOfRows),
      print: console.log,
    },
};
const wasm = new WebAssembly.Instance(wasmModule, imports);
const { part1 } = wasm.exports;

// run wasm
part1();