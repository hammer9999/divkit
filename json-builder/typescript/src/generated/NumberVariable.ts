// Generated code. Do not modify.

import { Exact, IntBoolean, NonEmptyArray } from '../helper';
import { TemplateBlock, Type } from '../template';
import { DivExpression } from '../safe-expression';

/**
 * A floating-point variable.
 */
export class NumberVariable<T extends NumberVariableProps = NumberVariableProps> {
    readonly _props?: Exact<NumberVariableProps, T>;

    readonly type = 'number';
    /**
     * Variable name.
     */
    name: Type<string>;
    /**
     * Value.
     */
    value: Type<number>;

    constructor(props: Exact<NumberVariableProps, T>) {
        this.name = props.name;
        this.value = props.value;
    }
}

export interface NumberVariableProps {
    /**
     * Variable name.
     */
    name: Type<string>;
    /**
     * Value.
     */
    value: Type<number>;
}
