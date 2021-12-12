# Try it here: https://www.tutorialspoint.com/execute_ruby_online.php

class Board
    attr_accessor :flashes

    def initialize(lines)
        @flashes = 0
        @rows = lines.map { |line|
            line.strip.chars.map { |char| Octopus.new(char.to_i) }
        }
    end 
    
    def raise_energy
        @rows.flatten.each &:raise_energy
    end

    def propagate_flashes
        while @rows.flatten.any? &:can_flash do
            @rows.length.times do |x|
                @rows[x].length.times do |y|
                    try_flash x, y
                end
            end
        end
    end
    
    def use_up_energy
        @rows.flatten.each &:use_up_energy
    end

    def is_synchronized
        @rows.flatten.all? { |octopus| octopus.level == @rows[0][0].level }
    end
    
    def try_flash(x,y)
        octopus = @rows[x][y]
        if octopus.can_flash
            @flashes += 1
            octopus.flash
            try_raise x-1, y-1
            try_raise x-1, y
            try_raise x-1, y+1
            try_raise x  , y-1
            try_raise x  , y+1
            try_raise x+1, y-1
            try_raise x+1, y
            try_raise x+1, y+1
        end
    end
    
    def try_raise(x,y)
        return if not x.between? 0, @rows.length-1
        return if not y.between? 0, @rows[0].length-1
        @rows[x][y].raise_energy
    end
end

class Octopus
    attr_accessor :level

    def initialize(level)
        @level = level
        @flashed = false
    end
    
    def raise_energy
        @level = @level + 1
    end
    
    def can_flash
        @level > 9 and not @flashed
    end
    
    def flash
        @level = 0
        @flashed = true
    end
    
    def use_up_energy
        if @flashed
            @level = 0
            @flashed = false
        end
    end
end

board = Board.new(ARGF.readlines)
1000.times { |i|
    step = i + 1
    board.raise_energy
    board.propagate_flashes
    board.use_up_energy
    if step == 100
        puts "Part 1: #{board.flashes}"
    end
    if board.is_synchronized
        puts "Part 2: #{step}"
        break
    end
}