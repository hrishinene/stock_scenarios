# Abstract class Assembler for assembling link structures
from abc import ABC, abstractmethod
from model.assembly import Assembly
from model.link_stack import LinkStack


class Assembler(ABC):
    def __init__(self, link_stack: LinkStack):
        self.link_stack = link_stack

    @abstractmethod
    def assemble(self) -> Assembly:
        pass