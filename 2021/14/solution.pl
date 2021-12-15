#!/usr/bin/perl -l
# Usage: ./solution.pl sample.txt 10

use warnings;
use strict;

sub trim { 
    my $string = shift;
    $string =~ s/^\s+|\s+$//g;
    return $string;
};

# parse file
open(my $file, '<', $ARGV[0]) or die $!;
my @lines = <$file>;
my @template = split(//, trim $lines[0]);
my %mappings = map { split(" -> ", trim $_) } @lines[2..$#lines];
close($file);

# create cache
my %letterPairsToCount;
$letterPairsToCount{$_}++ for
    map { join('', @template[$_, $_+1]) } # zip with next
    (0..$#template-1);

# run steps
foreach (0..$ARGV[1]-1) {
    my %copy = %letterPairsToCount;
    foreach my $letterPair (keys %letterPairsToCount) {
        my $insertion = $mappings{$letterPair};
        my @letters = split(//, $letterPair);
        my $left = join('', ($letters[0], $insertion));
        my $right = join('', ($insertion, $letters[1]));
        my $count = $letterPairsToCount{$letterPair};
        $copy{$letterPair} -= $count;
        $copy{$left} += $count;
        $copy{$right} += $count;
    }
    %letterPairsToCount = %copy;
}

# get count for each letter
my %letterToCount;
foreach (keys %letterPairsToCount) {
    my @letters = split(//, $_);
    $letterToCount{$letters[0]} += $letterPairsToCount{$_};
}
my $lastletter = $template[$#template];
$letterToCount{$lastletter}++;

# print result
my @sorted = sort { $a <=> $b } values %letterToCount;
my ($min, $max) = @sorted[0, $#sorted];
print $max - $min;
