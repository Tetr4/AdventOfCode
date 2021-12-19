// Compile and run: rustc solution.rs && ./solution sample1.txt

use std::fs::File;
use std::io::prelude::*;
use std::num::ParseIntError;
use std::cell::Cell;
use std::env;

struct Packet {
    version: u16,
    size: usize,
    payload: Payload
}

impl Packet {

    fn parse<T: FnMut(usize) -> u16 + Copy>(mut read_number: T) -> Packet {
        let version = read_number(3);
        let type_id = read_number(3);
        let payload = Payload::parse(type_id, read_number);
        Packet { 
            version: version,
            size: 6 + payload.size(),
            payload: payload,
         }
    }

    fn get_version_sum(&self) -> u64 {
        self.version as u64 + self.payload.get_version_sum()
    }

    fn evaluate(&self) -> u64 {
        self.payload.evaluate()
    }
}

enum Payload {
   Literal { value: u64, size: usize },
   Operator { type_id: u16, sub_packets: Vec<Packet>, size: usize },
}

impl Payload {

    fn parse<T: FnMut(usize) -> u16 + Copy>(type_id: u16, read_number: T) -> Payload {
        match type_id {
            4 => Payload::parse_literal(read_number),
            _ => Payload::parse_operator(type_id, read_number),
        }
    }

    fn parse_literal<T: FnMut(usize) -> u16 + Copy>(mut read_number: T) -> Payload {
        let mut result: u64 = 0;
        let mut groups = 0;
        loop {
            let is_last_group = read_number(1) == 0;
            result <<= 4;
            result |= read_number(4) as u64;
            groups += 1;
            if is_last_group {
                return Payload::Literal {
                    value: result,
                    size: groups * 5,
                }
            }
        }
    }

    fn parse_operator<T: FnMut(usize) -> u16 + Copy>(type_id: u16, mut read_number: T) -> Payload {
        let length_id = read_number(1);
        let mut sub_packets: Vec<Packet> = Vec::new();
        if length_id == 0 {
            let sub_packets_size = read_number(15) as usize;
            loop {
                let packet = Packet::parse(read_number);
                sub_packets.push(packet);
                let sub_size: usize = sub_packets.iter().map (|packet| packet.size).sum();
                if sub_size >= sub_packets_size {
                    break
                }
            }
        } else {
            let number_of_sub_packets = read_number(11);
            for _ in 0..number_of_sub_packets {
                let packet = Packet::parse(read_number);
                sub_packets.push(packet);
            }
        }
        let header_size: usize = 1 + if length_id == 0 { 15 } else { 11 };
        let sub_size: usize = sub_packets.iter().map (|packet| packet.size).sum();
        Payload::Operator { 
            size: header_size + sub_size,
            sub_packets,
            type_id,
        }
    }

    fn get_version_sum(&self) -> u64 {
        match self {
            Self::Literal { .. } => 0,
            Self::Operator { sub_packets, .. } => sub_packets.iter()
                .map (|packet| packet.get_version_sum())
                .sum(),
        }
    }

    fn size(&self) -> usize {
        match * self {
            Self::Literal { size, .. } => size,
            Self::Operator { size, .. } => size
        }
    }

    fn evaluate(&self) -> u64 {
        match self {
            Self::Literal { value, .. } => *value,
            Self::Operator { sub_packets, type_id: 0, .. } => {
                // sum
                sub_packets.iter().map (|packet| packet.evaluate()).sum()
            }
            Self::Operator { sub_packets, type_id: 1, .. } => {
                sub_packets.iter().map (|packet| packet.evaluate()).product()
            }
            Self::Operator { sub_packets, type_id: 2, .. } => {
                sub_packets.iter().map (|packet| packet.evaluate()).min().unwrap()
            }
            Self::Operator { sub_packets, type_id: 3, .. } => {
                sub_packets.iter().map (|packet| packet.evaluate()).max().unwrap()
            }
            Self::Operator { sub_packets, type_id: 5, .. } => {
                (sub_packets[0].evaluate() > sub_packets[1].evaluate()) as u64
            }
            Self::Operator { sub_packets, type_id: 6, .. } => {
                (sub_packets[0].evaluate() < sub_packets[1].evaluate()) as u64
            },
            Self::Operator { sub_packets, type_id: 7, .. } => {
                (sub_packets[0].evaluate() == sub_packets[1].evaluate()) as u64
            }
            _ => 0
        }
    }
}

fn hex_string_to_u8(input: &str) -> Result<Vec<u8>, ParseIntError> {
    (0..input.len())
        .step_by(2)
        .map(|i| u8::from_str_radix(&input[i..i + 2], 16))
        .collect()
}

fn main() -> Result<(), Box<dyn std::error::Error>> {
    // read byte data
    let args: Vec<String> = env::args().collect();
    let mut file = File::open(&args[1])?;
    let mut input = String::new();
    file.read_to_string(&mut input)?;
    let bytes: Vec<u8> = hex_string_to_u8(&input)?;

    // closure for parsing data
    let cursor = Cell::new(0);
    let read_number = |length: usize| -> u16 {
        let end = cursor.get() + length;
        cursor.set(cursor.get() + length);
        let end_index = end / 8;
        let mut data = bytes[end_index] as u32;
        if end_index >= 1 {
            data |= (bytes[end_index - 1] as u32) << 8;
        }
        if end_index >= 2 {
            data |= (bytes[end_index - 2] as u32) << 16;
        }
        let bit_offset = 8 - (end % 8);
        if bit_offset > 0 {
            data >>= bit_offset;
        }
        let length_mask = 2u32.pow(length as u32) - 1;
        (data & length_mask) as u16
    };

    // solution
    let root = Packet::parse(read_number);
    println!("Part 1: {}", root.get_version_sum());
    println!("Part 2: {}", root.evaluate());

    Ok(())
}
