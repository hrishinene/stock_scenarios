from model.assembly import Assembly
from model.geometry.point import Point
from model.geometry.vector import Vector
from model.link_stack import LinkStack
from model.pivot import Pivot
from model.placed_link import PlacedLink
from model.placement import Placement
from process.assembler import Assembler


class SimpleAssembler(Assembler):
    def __init__(self, link_stack: LinkStack):
        self.link_stack = link_stack

    def assemble(self):
        # Simple assembly logic place the links in default positions
        assembly = Assembly()
        pivot = Pivot(Placement(Point(0, 0, 0), Vector(0, 0, 1)))  # Default pivot along z-axis
        while self.link_stack.size() > 0:
            link = self.link_stack.pop_link()
            placed_link = PlacedLink(link, pivot)
            assembly.place_link(placed_link)
            pivot = placed_link.end_pivot  # Update pivot to the end of the last placed link
            
        return assembly
        
        