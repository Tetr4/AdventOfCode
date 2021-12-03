;; try it here: https://webassembly.github.io/wabt/demo/wat2wasm/

;; Algorithm:
;;
;; columnCount = 0
;; while (input[columnCount] != '\n') columnCount++;
;;
;; gamma = 0
;; for column in range(columnCount):
;;    difference = 0
;;    for row in input:
;;        if row[column] == '1':
;;            difference++
;;        else
;;            difference--
;;    if (difference > 0):
;;        gamma = gamma with bit at column set to 1
;; epsilon = gamma inverted and masked
;; return gamma * epsilon

(module
  ;; imports
  (import "js" "print" (func $print (param i32)))
  (import "js" "mem" (memory 1))
  (global $numberOfRows (import "js" "numberOfRows") i32)

  (func (export "part1")
    (local $columnCount i32)
    (local $column i32)
    (local $gamma i32)
    (local $epsilon i32)
    ;; columnCount = getColumnCount()
    call $getColumnCount
    local.set $columnCount
    ;; loop over columns
    (loop $readNextColumn
       ;; if getDigitsDifference(column, columnCount) > 0
      local.get $column
      local.get $columnCount
      call $getDigitsDifference
      i32.const 0
      i32.gt_s
      (if
        (then
          ;; gamma = setBit(gamma, column, columnCount)
          local.get $gamma
          local.get $column
          local.get $columnCount
          call $setBit
          local.set $gamma))
      ;; column++
      local.get $column
      i32.const 1
      i32.add
      local.set $column
      ;; if column < columnCount
      local.get $column
      local.get $columnCount
      i32.lt_s
      (if
        (then
          ;; resume loop
          br $readNextColumn)))
    ;; epsilon = getEpsilon(gamma, columnCount)
    local.get $gamma
    local.get $columnCount
    call $getEpsilon
    local.set $epsilon
    ;; print(epsilon * gamma)
    local.get $gamma
    local.get $epsilon
    i32.mul
    call $print)

  (func $getColumnCount (result i32)
    (local $i i32)
    ;; loop over columns
    (loop $nextColumn
      ;; if input[i] != '\n'
      local.get $i
      i32.load8_s
      i32.const 10 ;; '\n'
      i32.ne
      (if
        (then
          ;; i++ and resume loop;
          local.get $i
          i32.const 1
          i32.add
          local.set $i
          br $nextColumn)))
    ;; return i
    local.get $i)

  (func $getDigitsDifference (param $column i32) (param $columnCount i32) (result i32)
    (local $difference i32)
    (local $row i32)
    (loop $nextRow
      ;; if digit(row, column, columnCount) == '1'
      local.get $row
      local.get $column
      local.get $columnCount
      call $getDigit
      i32.const 49 ;; '1'
      i32.eq
      (if
        (then
          ;; difference++
          local.get $difference
          i32.const 1
          i32.add
          local.set $difference)
        (else
          ;; difference--
          local.get $difference
          i32.const 1
          i32.sub
          local.set $difference))
      ;; row++
      local.get $row
      i32.const 1
      i32.add
      local.set $row
      ;; if row < numberOfRows
      local.get $row
      global.get $numberOfRows
      i32.lt_s
      (if
        (then
          ;; resume loop
          br $nextRow)))
    ;; return difference
    local.get $difference)

  (func $setBit (param $gamma i32) (param $column i32) (param $columnCount i32) (result i32)
    ;; byte = 1 << (columnCount - (column + 1))
    ;; return gamma | byte
    local.get $gamma
    i32.const 1
    local.get $columnCount
    local.get $column
    i32.const 1
    i32.add
    i32.sub
    i32.shl
    i32.or)

  (func $getEpsilon (param $gamma i32) (param $columnCount i32) (result i32)
    ;; wasm does not have negation, so we have to juggle some bits
    ;; return gamma ^ ((1 << columnCount) - 1))
    i32.const 1
    local.get $columnCount
    i32.shl
    i32.const 1
    i32.sub
    local.get $gamma
    i32.xor)

  (func $getDigit (param $row i32) (param $column i32) (param $columnCount i32) (result i32)
    ;; don't forget to skip the newlines
    ;; return input[(columnCount + 1) * row + column]
    local.get $columnCount
    i32.const 1
    i32.add
    local.get $row
    i32.mul
    local.get $column
    i32.add
    i32.load8_s))
